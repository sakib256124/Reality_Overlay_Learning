package com.rola.app.data.vision

import android.graphics.Bitmap
import com.rola.app.data.ml.TensorFlowLiteClassifier
import com.rola.app.domain.model.BoundingBox
import com.rola.app.domain.model.DetectedObject
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.abs

@Singleton
class ObjectDetector @Inject constructor(
    private val classifier: TensorFlowLiteClassifier,
) {
    private val trackedObjects = mutableMapOf<String, DetectedObject>()
    private var activeModelFamily: VisionModelFamily = VisionModelFamily.ClassifierFallback

    fun setModelFamily(modelFamily: VisionModelFamily) {
        activeModelFamily = modelFamily
    }

    fun detectObjects(bitmap: Bitmap): List<DetectedObject> {
        val classifications = classifier.classify(bitmap, topK = maxObjectsFor(activeModelFamily))
        val now = System.currentTimeMillis()

        return classifications.mapIndexed { index, result ->
            val boundingBox = estimatedBoundingBox(index)
            val previous = findPreviousTrack(result.name, boundingBox)
            val trackingId = previous?.trackingId ?: "${result.name.normalizedId()}-${UUID.randomUUID()}"
            val detected = DetectedObject(
                id = result.name.normalizedId(),
                label = result.name,
                confidence = result.confidence,
                boundingBox = smoothBox(previous?.boundingBox, boundingBox),
                trackingId = trackingId,
                firstSeenAt = previous?.firstSeenAt ?: now,
                lastSeenAt = now,
                frameCount = (previous?.frameCount ?: 0) + 1,
                classification = result.name,
                isStable = (previous?.frameCount ?: 0) + 1 >= STABLE_FRAME_COUNT,
            )
            trackedObjects[trackingId] = detected
            detected
        }
            .also { pruneStaleTracks(now) }
    }

    fun clearTracking() {
        trackedObjects.clear()
    }

    private fun findPreviousTrack(label: String, boundingBox: BoundingBox): DetectedObject? =
        trackedObjects.values
            .filter { it.label.equals(label, ignoreCase = true) }
            .minByOrNull { distance(it.boundingBox, boundingBox) }
            ?.takeIf { distance(it.boundingBox, boundingBox) <= TRACKING_DISTANCE_THRESHOLD }

    private fun smoothBox(previous: BoundingBox?, current: BoundingBox): BoundingBox {
        if (previous == null) return current
        return BoundingBox(
            left = previous.left * SMOOTHING + current.left * (1f - SMOOTHING),
            top = previous.top * SMOOTHING + current.top * (1f - SMOOTHING),
            right = previous.right * SMOOTHING + current.right * (1f - SMOOTHING),
            bottom = previous.bottom * SMOOTHING + current.bottom * (1f - SMOOTHING),
        )
    }

    private fun distance(first: BoundingBox, second: BoundingBox): Float =
        abs(first.centerX - second.centerX) + abs(first.centerY - second.centerY)

    private fun pruneStaleTracks(now: Long) {
        trackedObjects.entries.removeIf { (_, objectTrack) ->
            now - objectTrack.lastSeenAt > TRACK_TTL_MILLIS
        }
    }

    private fun estimatedBoundingBox(index: Int): BoundingBox {
        val column = index % 2
        val row = index / 2
        val left = 0.08f + column * 0.42f
        val top = 0.16f + row * 0.26f
        return BoundingBox(
            left = left,
            top = top,
            right = (left + 0.36f).coerceAtMost(0.94f),
            bottom = (top + 0.24f).coerceAtMost(0.92f),
        )
    }

    private fun String.normalizedId(): String = lowercase()
        .replace(Regex("[^a-z0-9]+"), "_")
        .trim('_')
        .ifBlank { "object" }

    private fun maxObjectsFor(modelFamily: VisionModelFamily): Int =
        when (modelFamily) {
            VisionModelFamily.ClassifierFallback -> 5
            VisionModelFamily.EfficientDetLite -> 10
            VisionModelFamily.MobileNetSsd -> 8
            VisionModelFamily.YoloMobile -> 12
        }

    private companion object {
        const val STABLE_FRAME_COUNT = 3
        const val TRACK_TTL_MILLIS = 1_800L
        const val TRACKING_DISTANCE_THRESHOLD = 0.35f
        const val SMOOTHING = 0.65f
    }
}

enum class VisionModelFamily {
    ClassifierFallback,
    EfficientDetLite,
    MobileNetSsd,
    YoloMobile,
}
