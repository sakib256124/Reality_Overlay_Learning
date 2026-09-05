package com.rola.app.digital_companion.companion_core

enum class CompanionTone {
    FriendlyTeacher,
    ResearchMentor,
    CalmCoach,
    PracticePartner,
}

enum class CompanionMemoryType {
    CurrentConversation,
    CurrentLesson,
    CurrentObjective,
    LearningJourney,
    SkillDevelopment,
    EducationalHistory,
    Achievement,
    DifficultTopic,
    SuccessfulStrategy,
    Preference,
}

enum class CompanionModality {
    Text,
    Voice,
    ARGuidance,
    SpatialLearning,
    RobotInteraction,
    VirtualAvatar,
}

enum class CompanionConfidence {
    NeedsSupport,
    Developing,
    Confident,
}

data class CompanionLearningContext(
    val userId: String,
    val topic: String,
    val currentGoal: String,
    val recentMessage: String,
    val skillLevel: String,
    val recentScores: List<Int>,
    val preferredModalities: List<CompanionModality>,
)

data class CompanionMemoryRecord(
    val memoryId: String,
    val userId: String,
    val memoryType: CompanionMemoryType,
    val topic: String,
    val summary: String,
    val userControlled: Boolean,
    val updatedAt: Long = System.currentTimeMillis(),
)

data class CompanionPersonalityProfile(
    val profileId: String,
    val userId: String,
    val tone: CompanionTone,
    val motivationStyle: String,
    val explanationPreference: String,
)

data class CompanionEmotionState(
    val stateId: String,
    val motivationPercent: Int,
    val confidence: CompanionConfidence,
    val frustrationPercent: Int,
    val engagementPercent: Int,
    val recommendedResponse: String,
)

data class CompanionRelationshipState(
    val relationshipId: String,
    val userId: String,
    val progressSummary: String,
    val interactionHistory: List<String>,
    val goalsAchieved: List<String>,
    val learnsBestBy: String,
)

data class CompanionLearningPlan(
    val planId: String,
    val dailyStudyPlan: List<String>,
    val weeklyGoals: List<String>,
    val skillRoadmap: List<String>,
    val practiceSchedule: List<String>,
    val recommendations: List<String>,
)

data class CompanionDecision(
    val decisionId: String,
    val explanationMethod: String,
    val learningActivity: String,
    val difficultyLevel: String,
    val teachingApproach: String,
    val transparentReason: String,
)

data class CompanionConversationResponse(
    val responseId: String,
    val message: String,
    val modalities: List<CompanionModality>,
    val knowledgeSources: List<String>,
)

data class CompanionEvolutionState(
    val evolutionId: String,
    val improvements: List<String>,
    val futureRoadmap: List<String>,
    val updatedPreference: String,
)

data class CompanionPrivacyState(
    val privacyId: String,
    val memoryEnabled: Boolean,
    val cloudSyncEnabled: Boolean,
    val deletionControlAvailable: Boolean,
    val transparentDecisions: Boolean,
)

data class DigitalCompanionResult(
    val resultId: String,
    val companion: PersonalLearningCompanion,
    val memories: List<CompanionMemoryRecord>,
    val personality: CompanionPersonalityProfile,
    val emotionState: CompanionEmotionState,
    val relationshipState: CompanionRelationshipState,
    val learningPlan: CompanionLearningPlan,
    val decision: CompanionDecision,
    val conversationResponse: CompanionConversationResponse,
    val evolutionState: CompanionEvolutionState,
    val privacyState: CompanionPrivacyState,
)
