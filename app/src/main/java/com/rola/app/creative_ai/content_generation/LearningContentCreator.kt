package com.rola.app.creative_ai.content_generation

import com.rola.app.creative_ai.creativity_engine.CreativeAIRequest
import com.rola.app.creative_ai.creativity_engine.CreativeContentPackage
import javax.inject.Inject

class LearningContentCreator @Inject constructor() {
    fun create(request: CreativeAIRequest): CreativeContentPackage =
        CreativeContentPackage(
            contentId = "creative-content-${request.userId}-${request.topic.lowercase().replace(" ", "-")}",
            lessons = listOf("${request.level.name} lesson for ${request.topic}", "Objective: ${request.objective}"),
            examples = listOf("real-world ${request.topic} example", "visual explanation"),
            activities = listOf("guided exploration", "creative project prompt", "reflection checkpoint"),
            practiceMaterials = listOf("micro quiz", "case study", "experiment worksheet"),
        )
}
