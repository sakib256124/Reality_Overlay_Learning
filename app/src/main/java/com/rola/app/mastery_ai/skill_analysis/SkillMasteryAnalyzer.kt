package com.rola.app.mastery_ai.skill_analysis

import com.rola.app.mastery_ai.mastery_engine.MasteryLevel
import com.rola.app.mastery_ai.mastery_engine.MasteryRequest
import com.rola.app.mastery_ai.mastery_engine.SkillMasteryProfile
import javax.inject.Inject

class SkillMasteryAnalyzer @Inject constructor() {
    fun analyze(request: MasteryRequest): SkillMasteryProfile {
        val score = listOf(request.knowledgeLevel, request.practicalAbility, request.problemSolvingCapability, request.learningConsistency).average().toInt()
        val level = when {
            score >= 90 -> MasteryLevel.Expert
            score >= 75 -> MasteryLevel.Advanced
            score >= 55 -> MasteryLevel.Intermediate
            else -> MasteryLevel.Beginner
        }
        return SkillMasteryProfile(
            profileId = "mastery-profile-${request.learnerId}",
            skillName = request.skillName,
            masteryLevel = level,
            knowledgeLevel = request.knowledgeLevel,
            practicalAbility = request.practicalAbility,
            consistencyScore = request.learningConsistency,
            explainability = "Level is based on concept understanding, practice ability, problem solving, consistency, and previous performance.",
        )
    }
}
