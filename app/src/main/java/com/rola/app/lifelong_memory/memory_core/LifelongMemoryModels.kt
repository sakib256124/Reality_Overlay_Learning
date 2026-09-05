package com.rola.app.lifelong_memory.memory_core

enum class MemoryScope { ShortTerm, LongTerm }
enum class ExpertiseLevel { Beginner, Intermediate, Advanced, Expert }

data class LifelongMemoryContext(
    val userId: String,
    val currentLesson: String,
    val activeGoal: String,
    val recentInteractions: List<String>,
    val completedProjects: List<String>,
    val achievements: List<String>,
)

data class LifelongMemoryState(
    val memoryId: String,
    val userId: String,
    val shortTermMemory: List<String>,
    val longTermMemory: List<String>,
    val userOwned: Boolean,
)

data class PersonalKnowledgeGraphState(
    val graphId: String,
    val concepts: List<String>,
    val skills: List<String>,
    val experiences: List<String>,
    val achievements: List<String>,
    val expertise: ExpertiseLevel,
)

data class LearningExperienceState(
    val experienceId: String,
    val projects: List<String>,
    val experiments: List<String>,
    val researchWork: List<String>,
    val practicalSkills: List<String>,
)

data class MemoryRetrievalResult(
    val retrievalId: String,
    val recalledLessons: List<String>,
    val pastMistakes: List<String>,
    val learningPreferences: List<String>,
    val personalizedExplanation: String,
)

data class MemoryEvolutionState(
    val evolutionId: String,
    val organizedKnowledge: List<String>,
    val outdatedInformationRemoved: List<String>,
    val newConnections: List<String>,
)

data class LifelongMentorPlan(
    val mentorId: String,
    val lifelongGoals: List<String>,
    val careerLearningPath: List<String>,
    val recommendedSkills: List<String>,
    val futureRoadmap: List<String>,
)

data class MemorySecurityState(
    val securityId: String,
    val encrypted: Boolean,
    val exportAvailable: Boolean,
    val deletionAvailable: Boolean,
    val accessPermissions: List<String>,
)

data class LifelongMemoryAnalytics(
    val analyticsId: String,
    val knowledgeGrowthScore: Int,
    val retrievalQualityScore: Int,
    val personalizationScore: Int,
)

data class LifelongMemoryResult(
    val resultId: String,
    val memory: LifelongMemoryState,
    val graph: PersonalKnowledgeGraphState,
    val experience: LearningExperienceState,
    val retrieval: MemoryRetrievalResult,
    val evolution: MemoryEvolutionState,
    val mentorPlan: LifelongMentorPlan,
    val security: MemorySecurityState,
    val analytics: LifelongMemoryAnalytics,
)
