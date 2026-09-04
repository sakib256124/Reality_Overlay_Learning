package com.rola.app.data.vision

import android.graphics.Bitmap
import com.rola.app.domain.model.DetectionResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.system.measureTimeMillis

@Singleton
class VisionProcessor @Inject constructor(
    private val objectDetector: ObjectDetector,
    private val sceneAnalyzer: SceneAnalyzer,
) {
    fun processFrame(bitmap: Bitmap): DetectionResult {
        lateinit var objects: List<com.rola.app.domain.model.DetectedObject>
        val inferenceTime = measureTimeMillis {
            objects = objectDetector.detectObjects(bitmap)
        }
        val scene = sceneAnalyzer.analyzeScene(objects)
        val relationships = sceneAnalyzer.analyzeRelationships(objects)
        return DetectionResult(
            objects = objects,
            sceneContext = scene,
            relationships = relationships,
            inferenceTimeMillis = inferenceTime,
        )
    }

    fun resetTracking() {
        objectDetector.clearTracking()
    }
}
