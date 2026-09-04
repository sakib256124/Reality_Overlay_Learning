package com.rola.app.domain.model

data class UnifiedLearningContext(
    val userId: String,
    val learningLevel: SkillLevel,
    val previousKnowledge: List<String>,
    val preferences: LearningPreferences,
    val language: String,
    val detectedObjects: List<DetectedObject> = emptyList(),
    val sceneContext: SceneContext? = null,
    val locationContext: String? = null,
    val currentObjective: String,
    val knowledgeGraphContext: String = "",
    val researchContext: String = "",
)

data class LearningPreferences(
    val personalizationEnabled: Boolean = true,
    val learningSpeed: LearningSpeed = LearningSpeed.Balanced,
    val favoriteCategories: List<String> = emptyList(),
    val interestAreas: List<String> = emptyList(),
)

data class AIRequest(
    val action: UserLearningAction,
    val query: String = "",
    val objectId: String? = null,
    val context: UnifiedLearningContext? = null,
)

data class AIResponse(
    val selectedAgents: List<AIAgentType>,
    val message: String,
    val confidence: Float,
    val recommendedNextAction: UserLearningAction,
    val insights: List<String> = emptyList(),
)

enum class UserLearningAction {
    StartSession,
    DetectObject,
    RetrieveKnowledge,
    ShowArVisualization,
    ExplainWithTutor,
    TranslateContent,
    GenerateQuiz,
    AssessProgress,
    RecommendNext,
    ResearchExpansion,
    RecordAnalytics,
}

enum class AIAgentType {
    VisionAgent,
    KnowledgeAgent,
    TutorAgent,
    LearningAgent,
    ResearchAgent,
    AnalyticsAgent,
    TranslationAgent,
    QuizAgent,
}

data class LearningSessionState(
    val sessionId: String,
    val userId: String,
    val objective: String,
    val status: LearningSessionStatus = LearningSessionStatus.Started,
    val context: UnifiedLearningContext,
    val completedSteps: List<UserLearningAction> = emptyList(),
    val summary: LearningSessionSummary? = null,
    val startedAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)

enum class LearningSessionStatus {
    Started,
    Detecting,
    Learning,
    Assessing,
    Completed,
}

data class LearningSessionSummary(
    val sessionId: String,
    val learnedTopics: List<String>,
    val masterySignals: List<String>,
    val nextRecommendations: List<String>,
    val totalSteps: Int,
    val durationMillis: Long,
)

data class PersonalLearningReport(
    val userId: String,
    val learningGrowth: String,
    val knowledgeMastery: Int,
    val interestAreas: List<String>,
    val learningPatterns: List<String>,
    val performanceTrends: List<String>,
    val aiInsights: List<String>,
)

data class EducationDashboardState(
    val profile: LearningProfile?,
    val progress: LearningProgress?,
    val knowledgeMapSummary: String,
    val achievements: List<String>,
    val recommendations: List<Recommendation>,
    val report: PersonalLearningReport?,
)

data class InstitutionClassroom(
    val classroomId: String,
    val name: String,
    val teacherId: String,
    val studentIds: List<String>,
    val domain: String,
)

data class TeacherDashboardState(
    val teacherId: String,
    val classrooms: List<InstitutionClassroom>,
    val studentProgressSummary: Map<String, Int>,
    val recommendedMaterials: List<LearningMaterial>,
    val analyticsHighlights: List<String>,
)
