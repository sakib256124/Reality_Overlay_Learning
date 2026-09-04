package com.rola.app.cognitive_ai

import com.rola.app.data.database.CognitiveAIDao
import com.rola.app.data.database.entities.BehaviorAnalyticsEntity
import com.rola.app.data.database.entities.CognitiveAIDecisionEntity
import com.rola.app.data.database.entities.CognitiveActivityEntity
import com.rola.app.data.database.entities.CognitiveLearnerModelEntity
import com.rola.app.data.database.entities.CognitiveMemoryRecordEntity
import com.rola.app.data.database.entities.CognitiveProfileEntity
import com.rola.app.data.database.entities.CognitiveSkillMapEntity
import com.rola.app.data.database.entities.EmotionAnalyticsEntity
import com.rola.app.data.database.entities.LearningPatternEntity
import com.rola.app.data.database.entities.LearningPredictionEntity
import com.rola.app.data.database.entities.PersonalLearningPlanEntity
import com.rola.app.cognitive_ai.brain.CognitiveLearningResult
import com.rola.app.domain.model.CognitiveConsent
import com.rola.app.domain.model.CognitiveDashboardState
import com.rola.app.domain.model.CognitiveLearningActivity
import com.rola.app.domain.model.CognitiveRecommendation
import com.rola.app.domain.model.CognitiveSkill
import com.rola.app.domain.model.CognitiveTrend
import com.rola.app.domain.model.LearnerCognitiveProfile
import com.rola.app.domain.model.LearningSpeed
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

@Singleton
class CognitiveAIRepository @Inject constructor(
    private val cognitiveAIDao: CognitiveAIDao,
) {
    suspend fun recentActivities(userId: String): List<CognitiveLearningActivity> =
        cognitiveAIDao.recentActivities(userId).map {
            CognitiveLearningActivity(it.activityId, it.userId, it.activityType, it.topic, it.durationMillis, it.score, it.mistake, it.timestamp)
        }.asReversed()

    suspend fun saveResult(result: CognitiveLearningResult) {
        val profile = result.profile
        cognitiveAIDao.upsertProfile(profile.toEntity())
        cognitiveAIDao.upsertLearnerModel(
            CognitiveLearnerModelEntity(
                modelId = "cognitive-model-${profile.userId}",
                userId = profile.userId,
                knowledgeSummary = "Strong: ${profile.knowledgeStrengths.joinToString()}",
                learningProblemSummary = "Weak: ${profile.knowledgeWeaknesses.joinToString()}",
                preferredStrategy = result.decision.learningEnvironment,
                updatedAt = profile.updatedAt,
            ),
        )
        cognitiveAIDao.upsertMemoryRecords(result.memoryEncoding.lines().filter { it.isNotBlank() }.map {
            val parts = it.split(":", limit = 3)
            CognitiveMemoryRecordEntity(
                memoryId = "memory-${UUID.randomUUID()}",
                userId = profile.userId,
                memoryType = runCatching { com.rola.app.domain.model.CognitiveMemoryType.valueOf(parts.getOrElse(0) { "LearningPattern" }) }
                    .getOrDefault(com.rola.app.domain.model.CognitiveMemoryType.LearningPattern),
                topic = parts.getOrElse(1) { "Learning" },
                summary = parts.getOrElse(2) { it },
                strength = 70,
                updatedAt = System.currentTimeMillis(),
            )
        })
        cognitiveAIDao.upsertLearningPatterns(profile.knowledgeWeaknesses.map {
            LearningPatternEntity("learning-pattern-${UUID.randomUUID()}", profile.userId, "Needs spaced practice for $it", 0.8f, System.currentTimeMillis())
        })
        cognitiveAIDao.upsertBehaviorAnalytics(result.behaviorReport.toEntity())
        cognitiveAIDao.upsertEmotionAnalytics(result.emotionReport.toEntity())
        cognitiveAIDao.upsertSkillMaps(profile.skillDevelopment.map { it.toEntity(profile.userId) })
        cognitiveAIDao.upsertPrediction(result.prediction.toEntity())
        cognitiveAIDao.upsertPersonalPlan(result.mentorPlan.toEntity())
        cognitiveAIDao.upsertDecision(result.decision.toEntity())
    }

    suspend fun saveActivities(activities: List<CognitiveLearningActivity>) {
        cognitiveAIDao.upsertActivities(activities.map {
            CognitiveActivityEntity(it.activityId, it.userId, it.activityType, it.topic, it.durationMillis, it.score, it.mistake, it.timestamp)
        })
    }

    fun observeDashboard(userId: String): Flow<CognitiveDashboardState> =
        combine(
            cognitiveAIDao.observeProfile(userId).map { it?.toDomain() },
            cognitiveAIDao.observeSkillMap(userId).map { rows -> rows.map { it.toDomain() } },
            cognitiveAIDao.observeDecisions(userId),
        ) { profile, skills, decisions ->
            CognitiveDashboardState(
                profile = profile,
                intelligenceScore = profile?.intelligenceScore ?: 0,
                skillMap = skills,
                knowledgeGrowth = skills.filter { it.growthTrend == CognitiveTrend.Improving || it.growthTrend == CognitiveTrend.Accelerating }.map { "${it.name} is improving." },
                strengthAreas = profile?.knowledgeStrengths.orEmpty(),
                weakAreas = profile?.knowledgeWeaknesses.orEmpty(),
                futurePredictions = decisions.take(3).map { it.explanation },
                personalizedRecommendations = decisions.take(3).map {
                    CognitiveRecommendation(
                        recommendationId = "observed-rec-${it.decisionId}",
                        userId = it.userId,
                        nextLesson = it.nextTopic,
                        practiceActivities = it.recommendedActivities,
                        arExperience = it.learningEnvironment,
                        researchTopics = listOf("${it.nextTopic} research"),
                        quizDifficulty = it.difficultyLevel,
                        rationale = it.explanation,
                    )
                },
            )
        }
}

private fun LearnerCognitiveProfile.toEntity(): CognitiveProfileEntity = CognitiveProfileEntity(
    profileId,
    userId,
    learningLevel,
    learningStyle,
    knowledgeStrengths,
    knowledgeWeaknesses,
    preferredLearningMethod,
    learningSpeed.name,
    memoryAbility,
    intelligenceScore,
    consent.cognitiveAnalysisEnabled,
    consent.emotionAnalysisEnabled,
    consent.cloudProcessingEnabled,
    updatedAt,
)

private fun CognitiveProfileEntity.toDomain(): LearnerCognitiveProfile = LearnerCognitiveProfile(
    profileId = profileId,
    userId = userId,
    learningLevel = learningLevel,
    learningStyle = learningStyle,
    knowledgeStrengths = knowledgeStrengths,
    knowledgeWeaknesses = knowledgeWeaknesses,
    preferredLearningMethod = preferredLearningMethod,
    learningSpeed = runCatching { LearningSpeed.valueOf(learningSpeed) }.getOrDefault(LearningSpeed.Balanced),
    memoryAbility = memoryAbility,
    skillDevelopment = emptyList(),
    learningGoals = emptyList(),
    intelligenceScore = intelligenceScore,
    consent = CognitiveConsent(cognitiveAnalysisEnabled, emotionAnalysisEnabled, cloudProcessingEnabled),
    updatedAt = updatedAt,
)

private fun CognitiveSkill.toEntity(userId: String): CognitiveSkillMapEntity =
    CognitiveSkillMapEntity(skillId, userId, name, mastery, growthTrend.name)

private fun CognitiveSkillMapEntity.toDomain(): CognitiveSkill =
    CognitiveSkill(skillId, name, mastery, runCatching { CognitiveTrend.valueOf(growthTrend) }.getOrDefault(CognitiveTrend.Stable))

private fun com.rola.app.domain.model.LearningBehaviorReport.toEntity(): BehaviorAnalyticsEntity =
    BehaviorAnalyticsEntity(reportId, userId, studyDurationMinutes, learningFrequency, objectScanningPattern, quizAttemptPattern, questionPattern, contentInteractionPattern, recommendedMethod)

private fun com.rola.app.domain.model.EmotionLearningReport.toEntity(): EmotionAnalyticsEntity =
    EmotionAnalyticsEntity(reportId, userId, engagement.name, frustrationRisk, motivation.name, confidence, recommendedAdjustment)

private fun com.rola.app.domain.model.LearningPrediction.toEntity(): LearningPredictionEntity =
    LearningPredictionEntity(predictionId, userId, futurePerformance, predictedDifficulties, predictedKnowledgeGaps, skillImprovement, requiredLearningPath)

private fun com.rola.app.domain.model.PersonalLearningPlan.toEntity(): PersonalLearningPlanEntity =
    PersonalLearningPlanEntity(planId, userId, dailyGuidance, studyPlan, motivationalMessage, weaknessExplanation)

private fun com.rola.app.domain.model.CognitiveDecision.toEntity(): CognitiveAIDecisionEntity =
    CognitiveAIDecisionEntity(decisionId, userId, nextTopic, difficultyLevel, teachingStyle, assessmentType, learningEnvironment, recommendedActivities, explanation, requiresConsent)
