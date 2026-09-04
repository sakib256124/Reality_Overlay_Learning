package com.rola.app.data.vision

import com.rola.app.domain.model.DetectedObject
import com.rola.app.domain.model.ObjectRelationship
import com.rola.app.domain.model.SceneContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SceneAnalyzer @Inject constructor() {
    fun analyzeScene(objects: List<DetectedObject>): SceneContext {
        val labels = objects.map { it.label.lowercase() }
        val scene = when {
            labels.anyIn(kitchenObjects) -> "Kitchen"
            labels.anyIn(laboratoryObjects) -> "Laboratory"
            labels.anyIn(studyObjects) -> "Study Space"
            labels.anyIn(natureObjects) -> "Natural Environment"
            labels.isEmpty() -> "Unknown"
            else -> "General Learning Scene"
        }

        return SceneContext(
            sceneId = scene.lowercase().replace(" ", "_"),
            sceneType = scene,
            description = descriptionFor(scene),
            confidence = confidenceFor(scene, labels),
            detectedCategories = categoriesFor(labels),
            recommendedTopics = topicsFor(scene, labels),
            safetyInformation = safetyFor(scene, labels),
            scientificExplanations = scienceFor(scene, labels),
        )
    }

    fun analyzeRelationships(objects: List<DetectedObject>): List<ObjectRelationship> {
        val labels = objects.associateBy { it.label.lowercase() }
        return buildList {
            addRelationshipIfPresent(labels, "plant", "pot", "growing in", "A plant is growing inside a pot.")
            addRelationshipIfPresent(labels, "plant", "soil", "rooted in", "A plant uses soil for support, water, and minerals.")
            addRelationshipIfPresent(labels, "book", "laptop", "learning with", "A book and laptop together suggest mixed physical and digital learning.")
            addRelationshipIfPresent(labels, "cup", "table", "placed on", "A cup resting on a table shows a support relationship.")
            addRelationshipIfPresent(labels, "plate", "spoon", "used with", "A spoon is commonly used with a plate during eating.")
        }
    }

    private fun MutableList<ObjectRelationship>.addRelationshipIfPresent(
        labels: Map<String, DetectedObject>,
        subject: String,
        target: String,
        type: String,
        description: String,
    ) {
        val subjectObject = labels.entries.firstOrNull { subject in it.key }?.value ?: return
        val targetObject = labels.entries.firstOrNull { target in it.key }?.value ?: return
        add(
            ObjectRelationship(
                relationshipId = UUID.randomUUID().toString(),
                subjectObjectId = subjectObject.trackingId,
                objectObjectId = targetObject.trackingId,
                relationshipType = type,
                description = description,
                confidence = minOf(subjectObject.confidence, targetObject.confidence, 0.88f),
            ),
        )
    }

    private fun List<String>.anyIn(candidates: Set<String>): Boolean =
        any { label -> candidates.any { candidate -> candidate in label } }

    private fun descriptionFor(scene: String): String =
        when (scene) {
            "Kitchen" -> "This appears to be a kitchen or food-preparation environment."
            "Laboratory" -> "This appears to be a laboratory environment with science equipment."
            "Study Space" -> "This appears to be a study space for reading, writing, or computing."
            "Natural Environment" -> "This appears to include natural objects suited for biology learning."
            "Unknown" -> "Point the camera at objects to build a scene understanding."
            else -> "This scene contains everyday objects that can be studied together."
        }

    private fun confidenceFor(scene: String, labels: List<String>): Float =
        if (scene == "Unknown") 0f else (0.55f + labels.size * 0.08f).coerceAtMost(0.92f)

    private fun categoriesFor(labels: List<String>): List<String> = buildList {
        if (labels.anyIn(kitchenObjects)) add("Food and utensils")
        if (labels.anyIn(laboratoryObjects)) add("Science equipment")
        if (labels.anyIn(studyObjects)) add("Learning tools")
        if (labels.anyIn(natureObjects)) add("Biology")
        if (isEmpty() && labels.isNotEmpty()) add("Recognized objects")
    }

    private fun topicsFor(scene: String, labels: List<String>): List<String> = buildList {
        when (scene) {
            "Kitchen" -> addAll(listOf("Nutrition", "Materials", "Heat and safety"))
            "Laboratory" -> addAll(listOf("Measurement", "Scientific method", "Chemical safety"))
            "Study Space" -> addAll(listOf("Digital literacy", "Information organization"))
            "Natural Environment" -> addAll(listOf("Plant anatomy", "Ecosystems"))
            else -> add("Object classification")
        }
        if (labels.size >= 2) add("Object relationships")
    }.distinct()

    private fun safetyFor(scene: String, labels: List<String>): List<String> = buildList {
        if (scene == "Kitchen") add("Be careful around sharp tools, hot surfaces, and liquids.")
        if (scene == "Laboratory") add("Do not touch chemicals or glassware without guidance.")
        if (labels.any { "knife" in it || "chemical" in it }) add("Keep a safe distance from hazardous objects.")
    }

    private fun scienceFor(scene: String, labels: List<String>): List<String> = buildList {
        if (labels.any { "plant" in it }) add("Plants use light, water, and carbon dioxide to make food.")
        if (labels.any { "cup" in it || "bottle" in it }) add("Containers show volume, material, and shape properties.")
        if (scene == "Laboratory") add("Lab tools help measure, observe, and test scientific ideas.")
    }

    private companion object {
        val kitchenObjects = setOf("refrigerator", "plate", "spoon", "cup", "food", "bottle", "knife")
        val laboratoryObjects = setOf("microscope", "glassware", "chemical", "beaker", "test tube")
        val studyObjects = setOf("book", "laptop", "computer", "pen", "notebook")
        val natureObjects = setOf("plant", "leaf", "flower", "soil", "pot")
    }
}
