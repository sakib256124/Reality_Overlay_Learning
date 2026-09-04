package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.rola.app.domain.model.CognitiveActivityType
import com.rola.app.domain.model.CognitiveLearningStyle
import com.rola.app.domain.model.CognitiveMemoryType
import com.rola.app.domain.model.MemoryAbility
import com.rola.app.domain.model.PreferredLearningMethod
import com.rola.app.domain.model.SkillLevel

@Entity(tableName = "cognitive_profiles", indices = [Index(value = ["userId"]), Index(value = ["learningLevel"])])
data class CognitiveProfileEntity(
    @PrimaryKey val profileId: String,
    val userId: String,
    val learningLevel: SkillLevel,
    val learningStyle: CognitiveLearningStyle,
    val knowledgeStrengths: List<String>,
    val knowledgeWeaknesses: List<String>,
    val preferredLearningMethod: PreferredLearningMethod,
    val learningSpeed: String,
    val memoryAbility: MemoryAbility,
    val intelligenceScore: Int,
    val cognitiveAnalysisEnabled: Boolean,
    val emotionAnalysisEnabled: Boolean,
    val cloudProcessingEnabled: Boolean,
    val updatedAt: Long,
)

@Entity(tableName = "cognitive_learner_models", indices = [Index(value = ["userId"])])
data class CognitiveLearnerModelEntity(
    @PrimaryKey val modelId: String,
    val userId: String,
    val knowledgeSummary: String,
    val learningProblemSummary: String,
    val preferredStrategy: String,
    val updatedAt: Long,
)

@Entity(tableName = "cognitive_memory_records", indices = [Index(value = ["userId"]), Index(value = ["memoryType"]), Index(value = ["topic"])])
data class CognitiveMemoryRecordEntity(
    @PrimaryKey val memoryId: String,
    val userId: String,
    val memoryType: CognitiveMemoryType,
    val topic: String,
    val summary: String,
    val strength: Int,
    val updatedAt: Long,
)

@Entity(tableName = "learning_patterns", indices = [Index(value = ["userId"])])
data class LearningPatternEntity(
    @PrimaryKey val patternId: String,
    val userId: String,
    val pattern: String,
    val confidence: Float,
    val updatedAt: Long,
)

@Entity(tableName = "behavior_analytics", indices = [Index(value = ["userId"])])
data class BehaviorAnalyticsEntity(
    @PrimaryKey val reportId: String,
    val userId: String,
    val studyDurationMinutes: Int,
    val learningFrequency: Int,
    val objectScanningPattern: String,
    val quizAttemptPattern: String,
    val questionPattern: String,
    val contentInteractionPattern: String,
    val recommendedMethod: PreferredLearningMethod,
)

@Entity(tableName = "emotion_analytics", indices = [Index(value = ["userId"])])
data class EmotionAnalyticsEntity(
    @PrimaryKey val reportId: String,
    val userId: String,
    val engagement: String,
    val frustrationRisk: Int,
    val motivation: String,
    val confidence: Int,
    val recommendedAdjustment: String,
)

@Entity(tableName = "cognitive_skill_maps", indices = [Index(value = ["userId"]), Index(value = ["name"])])
data class CognitiveSkillMapEntity(
    @PrimaryKey val skillId: String,
    val userId: String,
    val name: String,
    val mastery: Int,
    val growthTrend: String,
)

@Entity(tableName = "learning_predictions", indices = [Index(value = ["userId"])])
data class LearningPredictionEntity(
    @PrimaryKey val predictionId: String,
    val userId: String,
    val futurePerformance: Int,
    val predictedDifficulties: List<String>,
    val predictedKnowledgeGaps: List<String>,
    val skillImprovement: String,
    val requiredLearningPath: List<String>,
)

@Entity(tableName = "personal_learning_plans", indices = [Index(value = ["userId"])])
data class PersonalLearningPlanEntity(
    @PrimaryKey val planId: String,
    val userId: String,
    val dailyGuidance: List<String>,
    val studyPlan: List<String>,
    val motivationalMessage: String,
    val weaknessExplanation: List<String>,
)

@Entity(tableName = "cognitive_ai_decisions", indices = [Index(value = ["userId"]), Index(value = ["nextTopic"])])
data class CognitiveAIDecisionEntity(
    @PrimaryKey val decisionId: String,
    val userId: String,
    val nextTopic: String,
    val difficultyLevel: SkillLevel,
    val teachingStyle: CognitiveLearningStyle,
    val assessmentType: String,
    val learningEnvironment: String,
    val recommendedActivities: List<String>,
    val explanation: String,
    val requiresConsent: Boolean,
)

@Entity(tableName = "cognitive_activity_events", indices = [Index(value = ["userId"]), Index(value = ["activityType"]), Index(value = ["timestamp"])])
data class CognitiveActivityEntity(
    @PrimaryKey val activityId: String,
    val userId: String,
    val activityType: CognitiveActivityType,
    val topic: String,
    val durationMillis: Long,
    val score: Int?,
    val mistake: String?,
    val timestamp: Long,
)
