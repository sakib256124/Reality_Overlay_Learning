package com.rola.app.lifelong_memory

import com.rola.app.data.database.LifelongMemoryDao
import com.rola.app.data.database.entities.ExpertiseProfileEntity
import com.rola.app.data.database.entities.KnowledgeConnectionEntity
import com.rola.app.data.database.entities.LearningExperienceEntity
import com.rola.app.data.database.entities.LearningTimelineEntity
import com.rola.app.data.database.entities.LifelongMemoryEntity
import com.rola.app.data.database.entities.MemoryAnalyticsEntity
import com.rola.app.data.database.entities.MemoryHistoryEntity
import com.rola.app.data.database.entities.PersonalKnowledgeGraphEntity
import com.rola.app.data.database.entities.SkillEvolutionEntity
import com.rola.app.lifelong_memory.memory_core.LifelongMemoryResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class LifelongMemoryRepository @Inject constructor(private val dao: LifelongMemoryDao) {
    fun observeDashboard(userId: String): Flow<LifelongMemoryDashboardState> =
        combine(dao.observeMemory(userId), dao.observeGraph(), dao.observeTimeline(userId), dao.observeExpertise(), dao.observeAnalytics()) { memory, graph, timeline, expertise, analytics ->
            LifelongMemoryDashboardState(
                knowledgeGrowth = graph?.concepts.orEmpty() + graph?.skills.orEmpty(),
                skillTimeline = timeline?.milestones.orEmpty(),
                achievements = graph?.achievements.orEmpty(),
                expertiseLevel = expertise?.expertise.orEmpty(),
                futureGoals = timeline?.futureGoals.orEmpty(),
                recalledMemory = memory?.longTermMemory.orEmpty(),
                knowledgeGrowthScore = analytics?.knowledgeGrowthScore ?: 0,
                personalizationScore = analytics?.personalizationScore ?: 0,
            )
        }

    suspend fun save(result: LifelongMemoryResult) {
        dao.upsertMemory(LifelongMemoryEntity(result.memory.memoryId, result.memory.userId, result.memory.shortTermMemory, result.memory.longTermMemory, result.memory.userOwned))
        dao.upsertGraph(PersonalKnowledgeGraphEntity(result.graph.graphId, result.graph.concepts, result.graph.skills, result.graph.experiences, result.graph.achievements, result.graph.expertise.name))
        dao.upsertExperience(LearningExperienceEntity(result.experience.experienceId, result.experience.projects, result.experience.experiments, result.experience.researchWork, result.experience.practicalSkills))
        dao.upsertEvolution(SkillEvolutionEntity(result.evolution.evolutionId, result.evolution.organizedKnowledge, result.evolution.outdatedInformationRemoved, result.evolution.newConnections))
        dao.upsertHistory(MemoryHistoryEntity(result.retrieval.retrievalId, result.retrieval.recalledLessons, result.retrieval.pastMistakes, result.retrieval.learningPreferences, result.retrieval.personalizedExplanation))
        dao.upsertConnection(KnowledgeConnectionEntity("connection-${result.graph.graphId}", result.graph.graphId, "Personal graph linked with global Knowledge Graph.", result.evolution.newConnections))
        dao.upsertTimeline(LearningTimelineEntity("timeline-${result.memory.userId}", result.memory.userId, result.graph.experiences + result.graph.achievements, result.mentorPlan.futureRoadmap))
        dao.upsertExpertise(ExpertiseProfileEntity(result.mentorPlan.mentorId, result.graph.expertise.name, result.mentorPlan.careerLearningPath, result.mentorPlan.recommendedSkills))
        dao.upsertAnalytics(MemoryAnalyticsEntity(result.analytics.analyticsId, result.analytics.knowledgeGrowthScore, result.analytics.retrievalQualityScore, result.analytics.personalizationScore))
    }
}

data class LifelongMemoryDashboardState(
    val knowledgeGrowth: List<String> = emptyList(),
    val skillTimeline: List<String> = emptyList(),
    val achievements: List<String> = emptyList(),
    val expertiseLevel: String = "",
    val futureGoals: List<String> = emptyList(),
    val recalledMemory: List<String> = emptyList(),
    val knowledgeGrowthScore: Int = 0,
    val personalizationScore: Int = 0,
)
