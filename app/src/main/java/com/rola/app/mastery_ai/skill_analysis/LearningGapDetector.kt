package com.rola.app.mastery_ai.skill_analysis

import com.rola.app.mastery_ai.mastery_engine.LearningGapReport
import com.rola.app.mastery_ai.mastery_engine.MasteryRequest
import javax.inject.Inject

class LearningGapDetector @Inject constructor() {
    fun detect(request: MasteryRequest): LearningGapReport =
        LearningGapReport(
            gapId = "gap-${request.learnerId}",
            missingConcepts = request.misunderstoodTopics.ifEmpty { listOf("advanced ${request.skillName} concept") },
            weakSkills = listOfNotNull(
                "concept mastery".takeIf { request.knowledgeLevel < 70 },
                "practical application".takeIf { request.practicalAbility < 70 },
                "problem solving".takeIf { request.problemSolvingCapability < 70 },
            ),
            misunderstoodTopics = request.misunderstoodTopics,
            incorrectPatterns = listOf("moves forward before proving mastery", "needs more real-world transfer checks"),
            recommendation = "Review missing concepts, add targeted practice, and re-evaluate mastery continuously.",
        )
}
