package com.rola.app.ai_teacher.personalization

import com.rola.app.domain.model.AdaptiveTeachingPlan
import com.rola.app.domain.model.LearningProfile
import com.rola.app.domain.model.LearningSpeed
import com.rola.app.domain.model.SkillLevel
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdaptiveTeachingEngine @Inject constructor() {
    fun adapt(
        teacherId: String,
        profile: LearningProfile?,
    ): AdaptiveTeachingPlan {
        val level = profile?.learningLevel ?: SkillLevel.Beginner
        val speed = profile?.learningSpeed ?: LearningSpeed.Balanced
        val gaps = (profile?.weakAreas.orEmpty() + profile?.difficultConcepts.orEmpty()).distinct()
        val score = profile?.averageQuizScore ?: 0
        return AdaptiveTeachingPlan(
            planId = "adaptive-teaching-${UUID.randomUUID()}",
            teacherId = teacherId,
            studentLevel = level,
            explanationComplexity = when (level) {
                SkillLevel.Beginner -> "simple language, concrete examples, short steps"
                SkillLevel.Intermediate -> "balanced vocabulary, causal reasoning, guided comparison"
                SkillLevel.Advanced -> "scientific vocabulary, mechanisms, exceptions, evidence"
            },
            learningSpeed = speed,
            exampleStrategy = if (profile?.favoriteCategories?.isNotEmpty() == true) {
                "Use examples from ${profile.favoriteCategories.take(3).joinToString()}."
            } else {
                "Start with familiar classroom and home objects."
            },
            activityStrategy = when (speed) {
                LearningSpeed.SlowAndSteady -> "Use short AR activities with reflection pauses."
                LearningSpeed.Balanced -> "Alternate AR exploration, explanation, and quick checks."
                LearningSpeed.Fast -> "Use challenge-based activities and advanced extensions."
            },
            recommendedInterventions = gaps.map { "Review $it with a visual example and one retrieval question." }
                .ifEmpty { listOf("Run a short diagnostic quiz before the next module.") },
            easyExplanation = "Use concrete language, one visible example, and a short teacher model before students practice.",
            advancedExplanation = "Add mechanism, evidence quality, edge cases, and a transfer challenge once core understanding is stable.",
            extraPractice = gaps.map { "Give 3 scaffolded questions and 1 AR observation task for $it." }
                .ifEmpty { listOf("Give a five-question mixed review and one short AR reflection.") },
            knowledgeGaps = gaps,
            recommendations = buildList {
                if (score in 1..59) add("Reteach prerequisites before advancing.")
                if (speed == LearningSpeed.SlowAndSteady) add("Shorten explanations and increase check-ins.")
                if (level == SkillLevel.Advanced) add("Offer research extensions and student-designed experiments.")
                if (isEmpty()) add("Continue balanced explanation, AR activity, practice, and reflection cycles.")
            },
        )
    }
}
