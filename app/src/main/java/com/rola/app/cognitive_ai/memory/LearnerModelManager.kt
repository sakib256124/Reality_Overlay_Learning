package com.rola.app.cognitive_ai.memory

import com.rola.app.domain.model.CognitiveActivityType
import com.rola.app.domain.model.CognitiveConsent
import com.rola.app.domain.model.CognitiveLearningActivity
import com.rola.app.domain.model.CognitiveLearningGoal
import com.rola.app.domain.model.CognitiveLearningStyle
import com.rola.app.domain.model.CognitiveSkill
import com.rola.app.domain.model.CognitiveTrend
import com.rola.app.domain.model.LearnerCognitiveProfile
import com.rola.app.domain.model.LearningSpeed
import com.rola.app.domain.model.MemoryAbility
import com.rola.app.domain.model.PreferredLearningMethod
import com.rola.app.domain.model.SkillLevel
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LearnerModelManager @Inject constructor(
    private val adaptiveMemoryManager: AdaptiveMemoryManager,
) {
    fun buildProfile(
        userId: String,
        activities: List<CognitiveLearningActivity>,
        consent: CognitiveConsent = CognitiveConsent(true, false, false),
    ): LearnerCognitiveProfile {
        val records = adaptiveMemoryManager.buildMemoryRecords(activities)
        val strengths = adaptiveMemoryManager.knows(records)
        val weaknesses = adaptiveMemoryManager.strugglesWith(records)
        val average = activities.mapNotNull { it.score }.average().takeIf { !it.isNaN() } ?: 50.0
        val arHeavy = activities.count { it.activityType == CognitiveActivityType.ARObjectExploration || it.activityType == CognitiveActivityType.Simulation }
        val tutorHeavy = activities.count { it.activityType == CognitiveActivityType.TutorConversation }
        val preferred = when {
            arHeavy >= tutorHeavy && arHeavy > 0 -> PreferredLearningMethod.ARModel
            tutorHeavy > arHeavy -> PreferredLearningMethod.TutorConversation
            activities.any { it.activityType == CognitiveActivityType.Research } -> PreferredLearningMethod.ResearchReading
            else -> PreferredLearningMethod.PracticeQuiz
        }
        return LearnerCognitiveProfile(
            profileId = "cognitive-profile-${UUID.randomUUID()}",
            userId = userId,
            learningLevel = when {
                average >= 85 -> SkillLevel.Advanced
                average >= 65 -> SkillLevel.Intermediate
                else -> SkillLevel.Beginner
            },
            learningStyle = when (preferred) {
                PreferredLearningMethod.ARModel, PreferredLearningMethod.ThreeDExplanation, PreferredLearningMethod.Simulation -> CognitiveLearningStyle.Visual
                PreferredLearningMethod.TutorConversation -> CognitiveLearningStyle.Social
                PreferredLearningMethod.ResearchReading -> CognitiveLearningStyle.ReadingWriting
                PreferredLearningMethod.PracticeQuiz -> CognitiveLearningStyle.Reflective
            },
            knowledgeStrengths = strengths,
            knowledgeWeaknesses = weaknesses,
            preferredLearningMethod = preferred,
            learningSpeed = when {
                average >= 82 && weaknesses.size <= 1 -> LearningSpeed.Fast
                average < 60 || weaknesses.size >= 3 -> LearningSpeed.SlowAndSteady
                else -> LearningSpeed.Balanced
            },
            memoryAbility = when {
                weaknesses.size >= 4 -> MemoryAbility.NeedsRepetition
                average >= 85 -> MemoryAbility.Strong
                average >= 65 -> MemoryAbility.Stable
                else -> MemoryAbility.Developing
            },
            skillDevelopment = skillMap(activities, average.toInt()),
            learningGoals = weaknesses.take(3).map {
                CognitiveLearningGoal(
                    goalId = "cognitive-goal-${UUID.randomUUID()}",
                    title = "Improve $it",
                    targetConcept = it,
                    targetMastery = 80,
                    progress = activities.filter { activity -> activity.topic == it }.mapNotNull { activity -> activity.score }.average().takeIf { value -> !value.isNaN() }?.toInt() ?: 0,
                )
            },
            intelligenceScore = (average.toInt() + strengths.size * 4 - weaknesses.size * 5).coerceIn(0, 100),
            consent = consent,
        )
    }

    private fun skillMap(
        activities: List<CognitiveLearningActivity>,
        fallbackScore: Int,
    ): List<CognitiveSkill> = activities.groupBy { it.topic }.map { (topic, topicActivities) ->
        val scores = topicActivities.mapNotNull { it.score }
        val first = scores.firstOrNull() ?: fallbackScore
        val last = scores.lastOrNull() ?: fallbackScore
        CognitiveSkill(
            skillId = "cognitive-skill-${topic.lowercase().replace(Regex("[^a-z0-9]+"), "-").trim('-')}",
            name = topic,
            mastery = scores.average().takeIf { !it.isNaN() }?.toInt() ?: fallbackScore,
            growthTrend = when {
                last - first >= 15 -> CognitiveTrend.Accelerating
                last > first -> CognitiveTrend.Improving
                last < first -> CognitiveTrend.Declining
                else -> CognitiveTrend.Stable
            },
        )
    }
}
