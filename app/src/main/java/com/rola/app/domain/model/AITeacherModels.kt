package com.rola.app.domain.model

data class CurriculumRequest(
    val subject: String,
    val topic: String,
    val gradeLevel: String,
    val learningObjective: String,
    val durationWeeks: Int,
    val studentLevel: SkillLevel,
    val languageCode: String = "en",
)

data class GeneratedCurriculum(
    val curriculumId: String,
    val teacherId: String,
    val institutionId: String,
    val request: CurriculumRequest,
    val title: String,
    val overview: String,
    val modules: List<CurriculumModule>,
    val assessments: List<AITeacherAssessment>,
    val quality: CurriculumQualityReport,
    val approvalStatus: TeacherApprovalStatus = TeacherApprovalStatus.Draft,
    val ownerUserId: String = teacherId,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)

data class CurriculumModule(
    val moduleId: String,
    val title: String,
    val objective: String,
    val lessons: List<GeneratedLesson>,
    val estimatedHours: Int,
)

data class GeneratedLesson(
    val lessonId: String,
    val title: String,
    val objectives: List<String>,
    val explanation: String,
    val examples: List<String>,
    val experiments: List<String>,
    val arActivities: List<ARLearningActivity>,
    val practiceQuestions: List<String>,
    val difficulty: SkillLevel,
    val applications: List<String> = emptyList(),
    val summary: String = "",
    val teacherNotes: List<String> = emptyList(),
)

data class ARLearningActivity(
    val activityId: String,
    val title: String,
    val description: String,
    val suggestedObjectIds: List<String>,
    val activityType: TeachingActivityType,
    val estimatedMinutes: Int,
)

enum class TeachingActivityType {
    ArObservation,
    ThreeDExploration,
    VirtualExperiment,
    GroupChallenge,
    ResearchReflection,
}

data class AITeacherAssessment(
    val assessmentId: String,
    val title: String,
    val difficulty: SkillLevel,
    val mcqQuestions: List<String>,
    val practicalTasks: List<String>,
    val arAssignments: List<String>,
    val researchActivities: List<String>,
)

data class AdaptiveTeachingPlan(
    val planId: String,
    val teacherId: String,
    val studentLevel: SkillLevel,
    val explanationComplexity: String,
    val learningSpeed: LearningSpeed,
    val exampleStrategy: String,
    val activityStrategy: String,
    val recommendedInterventions: List<String>,
    val easyExplanation: String = "",
    val advancedExplanation: String = "",
    val extraPractice: List<String> = emptyList(),
    val knowledgeGaps: List<String> = emptyList(),
    val recommendations: List<String> = emptyList(),
)

data class CurriculumQualityReport(
    val reportId: String,
    val scientificAccuracy: Float,
    val levelAlignment: Float,
    val objectiveCoverage: Float,
    val contentConsistency: Float,
    val notes: List<String>,
) {
    val publishReady: Boolean
        get() = scientificAccuracy >= 0.75f &&
            levelAlignment >= 0.7f &&
            objectiveCoverage >= 0.75f &&
            contentConsistency >= 0.75f
}

enum class TeacherApprovalStatus {
    Draft,
    PendingReview,
    Approved,
    Rejected,
    Distributed,
}

data class TeacherReviewRecord(
    val reviewId: String,
    val contentId: String,
    val teacherId: String,
    val status: TeacherApprovalStatus,
    val comments: String,
    val reviewedAt: Long = System.currentTimeMillis(),
)

data class LessonAnalytics(
    val analyticsId: String,
    val lessonId: String,
    val classId: String,
    val completionRate: Int,
    val engagementScore: Int,
    val averageAssessmentScore: Int,
    val difficultySignal: SkillLevel,
    val improvementNotes: List<String>,
    val generatedAt: Long = System.currentTimeMillis(),
)

data class TeachingPlanRequest(
    val teacherId: String,
    val institutionId: String,
    val subject: String,
    val topic: String,
    val gradeLevel: String,
    val durationWeeks: Int,
    val classSize: Int,
    val studentLevel: SkillLevel,
)

data class AICurriculumTeachingPlan(
    val planId: String,
    val request: TeachingPlanRequest,
    val weeklySequence: List<String>,
    val lessonSuggestions: List<String>,
    val materialSuggestions: List<String>,
    val questionPrompts: List<String>,
    val studentAnalysisPrompts: List<String>,
)

data class TeacherSecurityContext(
    val teacherId: String,
    val institutionId: String,
    val permissions: Set<TeacherPermission>,
)

enum class TeacherPermission {
    GenerateCurriculum,
    ReviewContent,
    ApproveContent,
    DistributeContent,
    ViewStudentAnalytics,
}

data class AIGeneratedContentRecord(
    val contentId: String,
    val contentType: AIGeneratedContentType,
    val ownerUserId: String,
    val institutionId: String,
    val approvalStatus: TeacherApprovalStatus,
    val sourcePrompt: String,
    val qualityReportId: String,
    val createdAt: Long = System.currentTimeMillis(),
)

enum class AIGeneratedContentType {
    Course,
    Curriculum,
    Lesson,
    Activity,
    Assessment,
    TeachingPlan,
}

data class AITeacherAuditRecord(
    val auditId: String,
    val actorUserId: String,
    val institutionId: String,
    val action: String,
    val contentId: String,
    val timestamp: Long = System.currentTimeMillis(),
)

data class AITeacherDashboard(
    val generatedLessons: List<GeneratedLesson>,
    val curriculumPlans: List<GeneratedCurriculum>,
    val studentInsights: List<String>,
    val suggestedImprovements: List<String>,
    val pendingApprovals: List<GeneratedCurriculum>,
    val lessonAnalytics: List<LessonAnalytics>,
)

data class ClassroomAssistantResponse(
    val question: String,
    val answer: String,
    val examples: List<String>,
    val demonstrations: List<String>,
    val understandingSignal: String,
)
