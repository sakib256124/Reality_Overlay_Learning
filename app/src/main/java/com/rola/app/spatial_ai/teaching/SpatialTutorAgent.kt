package com.rola.app.spatial_ai.teaching

import com.rola.app.domain.model.SkillLevel
import com.rola.app.domain.model.SpatialObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpatialTutorAgent @Inject constructor() {
    fun explainObject(
        spatialObject: SpatialObject,
        question: String,
        level: SkillLevel,
    ): String = when (level) {
        SkillLevel.Beginner -> "${spatialObject.name} helps explain the idea visually. $question Look at its shape, labels, and movement."
        SkillLevel.Intermediate -> "${spatialObject.name} connects structure, evidence, and cause-effect reasoning. Rotate it and compare what changes."
        SkillLevel.Advanced -> "${spatialObject.name} can be used as a model for mechanisms, constraints, exceptions, and transfer to new cases."
    }

    fun voiceInstructions(objectName: String): List<String> = listOf(
        "Look at $objectName.",
        "Rotate it slowly.",
        "Point to the evidence.",
        "Explain what changed.",
    )

    fun visualDemonstration(topic: String): String =
        "Show labels, animate the key process, pause at the misconception point, and ask for a prediction about $topic."
}
