package com.rola.app.domain.model

data class LearningMaterial(
    val materialId: String,
    val topic: String,
    val materialType: LearningMaterialType,
    val title: String,
    val beginnerExplanation: String,
    val advancedExplanation: String,
    val summary: String,
    val quizQuestions: List<String>,
    val flashcards: List<Flashcard>,
    val sourceUpdateId: String,
    val version: Int = 1,
    val status: ContentApprovalStatus = ContentApprovalStatus.PendingApproval,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)

data class Flashcard(
    val front: String,
    val back: String,
)

enum class LearningMaterialType {
    ObjectDescription,
    EducationalArticle,
    QuizBank,
    PracticeExercise,
    StudyGuide,
}

enum class ContentApprovalStatus {
    Draft,
    PendingApproval,
    Approved,
    Rejected,
}

data class ContentVersion(
    val versionId: String,
    val contentId: String,
    val contentType: String,
    val version: Int,
    val sourceIds: List<String>,
    val changeSummary: String,
    val modifiedBy: String,
    val createdAt: Long = System.currentTimeMillis(),
)

data class ResearchDashboardState(
    val pendingTasks: List<ResearchTask> = emptyList(),
    val pendingUpdates: List<KnowledgeUpdate> = emptyList(),
    val trustedSources: List<ScientificSource> = emptyList(),
    val learningMaterials: List<LearningMaterial> = emptyList(),
    val statistics: ResearchStatistics = ResearchStatistics(),
)

data class ResearchStatistics(
    val pendingTaskCount: Int = 0,
    val approvalQueueCount: Int = 0,
    val trustedSourceCount: Int = 0,
    val generatedMaterialCount: Int = 0,
    val appliedUpdateCount: Int = 0,
)
