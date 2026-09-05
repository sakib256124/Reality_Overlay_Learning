package com.rola.app.mastery_ai.mastery_engine

enum class MasteryLevel { Beginner, Intermediate, Advanced, Expert }
enum class MasteryDecision { ContinuePractice, AdvanceDifficulty, StartProject, HumanReview }

data class MasteryRequest(
    val learnerId: String,
    val skillName: String,
    val knowledgeLevel: Int,
    val practicalAbility: Int,
    val problemSolvingCapability: Int,
    val learningConsistency: Int,
    val previousPerformance: List<Int>,
    val misunderstoodTopics: List<String>,
)

data class SkillMasteryProfile(val profileId: String, val skillName: String, val masteryLevel: MasteryLevel, val knowledgeLevel: Int, val practicalAbility: Int, val consistencyScore: Int, val explainability: String)
data class CompetencyScore(val scoreId: String, val conceptMastery: Int, val practicalApplication: Int, val criticalThinking: Int, val creativity: Int, val realWorldPerformance: Int, val level: MasteryLevel)
data class LearningGapReport(val gapId: String, val missingConcepts: List<String>, val weakSkills: List<String>, val misunderstoodTopics: List<String>, val incorrectPatterns: List<String>, val recommendation: String)
data class MasteryAdaptation(val adaptationId: String, val lessonDifficulty: String, val explanationStyle: String, val practiceActivities: List<String>, val learningSpeed: String, val assessmentMethod: String)
data class SkillImprovementPlan(val improvementId: String, val practiceTasks: List<String>, val projectRecommendations: List<String>, val learningChallenges: List<String>, val longTermImprovement: String)
data class MasteryTeachingPlan(val teachingId: String, val targetedTeaching: List<String>, val learnerFeedback: String, val humanReviewSupported: Boolean)
data class ProjectMasteryEvaluation(val projectId: String, val realWorldProjects: List<String>, val practicalAssessment: String, val portfolioEvidence: List<String>, val expertEvaluation: String)
data class ContinuousAssessmentResult(val assessmentId: String, val dailyProgress: Int, val practicalPerformance: Int, val knowledgeRetention: Int, val skillGrowth: Int, val fairnessExplanation: String)
data class MasteryAnalytics(val analyticsId: String, val masteryProgress: Int, val improvementAreas: List<String>, val futureRecommendations: List<String>, val biasCheck: String)
data class AdaptiveMasteryResult(
    val resultId: String,
    val profile: SkillMasteryProfile,
    val competency: CompetencyScore,
    val gapReport: LearningGapReport,
    val adaptation: MasteryAdaptation,
    val improvementPlan: SkillImprovementPlan,
    val teachingPlan: MasteryTeachingPlan,
    val projectEvaluation: ProjectMasteryEvaluation,
    val continuousAssessment: ContinuousAssessmentResult,
    val analytics: MasteryAnalytics,
    val decision: MasteryDecision,
)
