package com.rola.app.mastery_ai.mastery_engine

import javax.inject.Inject

class MasteryTeacherAgent @Inject constructor() {
    fun teach(profile: SkillMasteryProfile, gaps: LearningGapReport): MasteryTeachingPlan =
        MasteryTeachingPlan(
            teachingId = "teacher-${profile.profileId}",
            targetedTeaching = gaps.missingConcepts.map { "reteach $it with examples" }.ifEmpty { listOf("advance ${profile.skillName} toward expert transfer") },
            learnerFeedback = "Mastery level ${profile.masteryLevel.name} is explainable and can be reviewed by the learner.",
            humanReviewSupported = true,
        )
}
