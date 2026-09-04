package com.rola.app.domain.model

data class LearnerCognitiveProfile(
    val profileId: String,
    val userId: String,
    val learningLevel: SkillLevel,
    val learningStyle: CognitiveLearningStyle,
    val knowledgeStrengths: List<String>,
    val knowledgeWeaknesses: List<String>,
    val preferredLearningMethod: PreferredLearningMethod,
    val learningSpeed: LearningSpeed,
    val memoryAbility: MemoryAbility,
    val skillDevelopment: List<CognitiveSkill>,
    val learningGoals: List<CognitiveLearningGoal>,
    val intelligenceScore: Int,
    val consent: CognitiveConsent,
    val updatedAt: Long = System.currentTimeMillis(),
)

enum class CognitiveLearningStyle {
    Visual,
    Auditory,
    ReadingWriting,
    Kinesthetic,
    Social,
    Reflective,
}

enum class PreferredLearningMethod {
    ARModel,
    ThreeDExplanation,
    TutorConversation,
    PracticeQuiz,
    ResearchReading,
    Simulation,
}

enum class MemoryAbility {
    NeedsRepetition,
    Developing,
    Stable,
    Strong,
}

data class CognitiveSkill(
    val skillId: String,
    val name: String,
    val mastery: Int,
    val growthTrend: CognitiveTrend,
)

enum class CognitiveTrend {
    Declining,
    Stable,
    Improving,
    Accelerating,
}

data class CognitiveLearningGoal(
    val goalId: String,
    val title: String,
    val targetConcept: String,
    val targetMastery: Int,
    val progress: Int,
)

data class CognitiveConsent(
    val cognitiveAnalysisEnabled: Boolean,
    val emotionAnalysisEnabled: Boolean,
    val cloudProcessingEnabled: Boolean,
)

data class CognitiveLearningActivity(
    val activityId: String,
    val userId: String,
    val activityType: CognitiveActivityType,
    val topic: String,
    val durationMillis: Long,
    val score: Int? = null,
    val mistake: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
)

enum class CognitiveActivityType {
    Lesson,
    Quiz,
    ARObjectExploration,
    TutorConversation,
    Search,
    Simulation,
    Research,
}

data class CognitiveMemoryRecord(
    val memoryId: String,
    val userId: String,
    val memoryType: CognitiveMemoryType,
    val topic: String,
    val summary: String,
    val strength: Int,
    val updatedAt: Long = System.currentTimeMillis(),
)

enum class CognitiveMemoryType {
    ShortTermObjective,
    RecentMistake,
    LearnedConcept,
    Achievement,
    KnowledgeGap,
    LearningPattern,
}

data class LearningBehaviorReport(
    val reportId: String,
    val userId: String,
    val studyDurationMinutes: Int,
    val learningFrequency: Int,
    val objectScanningPattern: String,
    val quizAttemptPattern: String,
    val questionPattern: String,
    val contentInteractionPattern: String,
    val recommendedMethod: PreferredLearningMethod,
)

data class EmotionLearningReport(
    val reportId: String,
    val userId: String,
    val engagement: EngagementLevel,
    val frustrationRisk: Int,
    val motivation: MotivationLevel,
    val confidence: Int,
    val recommendedAdjustment: String,
)

enum class EngagementLevel {
    Low,
    Moderate,
    High,
}

enum class MotivationLevel {
    NeedsSupport,
    Steady,
    Strong,
}

data class LearningPrediction(
    val predictionId: String,
    val userId: String,
    val futurePerformance: Int,
    val predictedDifficulties: List<String>,
    val predictedKnowledgeGaps: List<String>,
    val skillImprovement: String,
    val requiredLearningPath: List<String>,
)

data class PersonalLearningPlan(
    val planId: String,
    val userId: String,
    val dailyGuidance: List<String>,
    val studyPlan: List<String>,
    val motivationalMessage: String,
    val weaknessExplanation: List<String>,
)

data class CognitiveRecommendation(
    val recommendationId: String,
    val userId: String,
    val nextLesson: String,
    val practiceActivities: List<String>,
    val arExperience: String,
    val researchTopics: List<String>,
    val quizDifficulty: SkillLevel,
    val rationale: String,
)

data class CognitiveDecision(
    val decisionId: String,
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

data class CognitiveDashboardState(
    val profile: LearnerCognitiveProfile?,
    val intelligenceScore: Int,
    val skillMap: List<CognitiveSkill>,
    val knowledgeGrowth: List<String>,
    val strengthAreas: List<String>,
    val weakAreas: List<String>,
    val futurePredictions: List<String>,
    val personalizedRecommendations: List<CognitiveRecommendation>,
)

data class CognitiveSecurityContext(
    val userId: String,
    val institutionId: String,
    val permissions: Set<CognitivePermission>,
    val consent: CognitiveConsent,
)

enum class CognitivePermission {
    AnalyzeLearner,
    ViewCognitiveProfile,
    GeneratePersonalPlan,
    MakeEducationalDecision,
    ViewEmotionAnalytics,
}
