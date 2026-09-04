package com.rola.app.unit

import com.rola.app.data.vision.SceneAnalyzer
import com.rola.app.domain.model.BoundingBox
import com.rola.app.domain.model.DetectedObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SceneAnalyzerTest {
    private val analyzer = SceneAnalyzer()

    @Test
    fun analyzeScene_detectsLaboratoryContext() {
        val scene = analyzer.analyzeScene(
            listOf(
                detectedObject("microscope"),
                detectedObject("glassware"),
                detectedObject("chemical bottle"),
            ),
        )

        assertEquals("Laboratory", scene.sceneType)
        assertTrue(scene.safetyInformation.isNotEmpty())
        assertTrue("Scientific method" in scene.recommendedTopics)
    }

    @Test
    fun analyzeRelationships_detectsPlantPotRelationship() {
        val relationships = analyzer.analyzeRelationships(
            listOf(
                detectedObject("plant"),
                detectedObject("pot"),
                detectedObject("soil"),
            ),
        )

        assertTrue(relationships.any { it.relationshipType == "growing in" })
        assertTrue(relationships.any { it.relationshipType == "rooted in" })
    }

    private fun detectedObject(label: String): DetectedObject =
        DetectedObject(
            id = label.replace(" ", "_"),
            label = label,
            confidence = 0.9f,
            boundingBox = BoundingBox(0.1f, 0.1f, 0.4f, 0.4f),
        )
}
