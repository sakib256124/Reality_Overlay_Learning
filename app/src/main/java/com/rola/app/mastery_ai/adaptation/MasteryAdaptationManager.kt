package com.rola.app.mastery_ai.adaptation

import com.rola.app.mastery_ai.mastery_engine.LearningGapReport
import com.rola.app.mastery_ai.mastery_engine.MasteryAdaptation
import com.rola.app.mastery_ai.mastery_engine.MasteryLevel
import com.rola.app.mastery_ai.mastery_engine.SkillMasteryProfile
import javax.inject.Inject

class MasteryAdaptationManager @Inject constructor() {
    fun adapt(profile: SkillMasteryProfile, gaps: LearningGapReport): MasteryAdaptation =
        MasteryAdaptation(
            adaptationId = "adapt-${profile.profileId}",
            lessonDifficulty = if (profile.masteryLevel == MasteryLevel.Beginner) "simpler foundations" else "progressive challenge",
            explanationStyle = if (gaps.missingConcepts.isNotEmpty()) "step-by-step visual explanation" else "concise expert explanation",
            practiceActivities = gaps.weakSkills.map { "targeted $it drill" }.ifEmpty { listOf("mixed mastery challenge") },
            learningSpeed = if (profile.consistencyScore < 70) "slower with review loops" else "accelerated with checkpoints",
            assessmentMethod = "continuous practical assessment with user feedback and human review.",
        )
}
