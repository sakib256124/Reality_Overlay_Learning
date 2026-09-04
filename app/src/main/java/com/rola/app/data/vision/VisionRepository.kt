package com.rola.app.data.vision

import android.graphics.Bitmap
import com.google.ar.core.Frame
import com.rola.app.domain.model.DetectionResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class VisionProcessingOutcome {
    data class Success(val result: DetectionResult) : VisionProcessingOutcome()
    data object NoFrame : VisionProcessingOutcome()
    data class Error(val message: String, val throwable: Throwable? = null) : VisionProcessingOutcome()
}

@Singleton
class VisionRepository @Inject constructor(
    private val imageFrameProcessor: ImageFrameProcessor,
    private val visionProcessor: VisionProcessor,
) {
    suspend fun processFrame(
        frame: Frame,
        dispatcher: CoroutineDispatcher = Dispatchers.Default,
    ): VisionProcessingOutcome = withContext(dispatcher) {
        val bitmap = imageFrameProcessor.frameToBitmap(frame)
            ?: return@withContext VisionProcessingOutcome.NoFrame
        processBitmap(bitmap, shouldRecycle = true)
    }

    suspend fun processBitmap(
        bitmap: Bitmap,
        shouldRecycle: Boolean = false,
        dispatcher: CoroutineDispatcher = Dispatchers.Default,
    ): VisionProcessingOutcome = withContext(dispatcher) {
        runCatching { visionProcessor.processFrame(bitmap) }
            .fold(
                onSuccess = { VisionProcessingOutcome.Success(it) },
                onFailure = { throwable ->
                    VisionProcessingOutcome.Error(
                        message = throwable.message ?: "Advanced vision processing failed.",
                        throwable = throwable,
                    )
                },
            )
            .also {
                if (shouldRecycle) bitmap.recycle()
            }
    }

    fun resetTracking() {
        visionProcessor.resetTracking()
    }
}
