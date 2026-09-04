package com.rola.app.core.agi

import com.rola.app.core.agi.memory.LearnerMemory
import com.rola.app.data.database.AGIDao
import com.rola.app.data.database.entities.AIDecisionEntity
import com.rola.app.data.database.entities.FutureRecommendationEntity
import com.rola.app.data.database.entities.KnowledgeEvolutionEntity
import com.rola.app.data.database.entities.LearnerModelEntity
import com.rola.app.data.database.entities.toAgentNames
import com.rola.app.data.database.entities.toEntity
import com.rola.app.domain.model.AGIDashboardState
import com.rola.app.domain.model.AGILearningEvent
import com.rola.app.domain.model.AGIOrchestrationResult
import com.rola.app.domain.model.FutureRecommendation
import com.rola.app.domain.model.KnowledgeEvolutionProposal
import com.rola.app.domain.model.TeacherApprovalStatus
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class AGIRepository @Inject constructor(
    private val agiDao: AGIDao,
    private val memoryManager: MemoryManager,
) {
    suspend fun loadMemory(
        learnerId: String,
        newEvent: AGILearningEvent? = null,
    ): LearnerMemory {
        val history = agiDao.recentMemory(learnerId).map { it.toDomain() }.asReversed()
        val goals = agiDao.learningGoals(learnerId).map { it.toDomain() }
        val memory = memoryManager.buildMemory(learnerId, history, goals)
        return newEvent?.let { memory.remember(it) } ?: memory
    }

    suspend fun saveMemory(memory: LearnerMemory, event: AGILearningEvent) {
        agiDao.upsertMemory(event.toEntity())
        agiDao.upsertLearnerModel(
            LearnerModelEntity(
                learnerId = memory.learnerId,
                knowledgeLevel = memory.knowledgeLevel,
                learningSpeed = memory.learningSpeed.name,
                retentionScore = memory.retentionScore,
                interests = memory.interests,
                previousMistakes = memory.previousMistakes,
                learningPatterns = memory.learningPatterns,
                updatedAt = memory.updatedAt,
            ),
        )
        agiDao.upsertSkillGraph(memory.skills.map { it.toEntity(memory.learnerId) })
    }

    suspend fun saveDecision(
        institutionId: String,
        learnerId: String,
        result: AGIOrchestrationResult,
    ) {
        agiDao.upsertDecision(
            AIDecisionEntity(
                decisionId = result.decisionId,
                learnerId = learnerId,
                institutionId = institutionId,
                selectedAgents = result.selectedAgents.toAgentNames(),
                actionTitle = result.learningAction.title,
                actionDescription = result.learningAction.description,
                verified = result.safetyReport.verified,
                requiresHumanApproval = result.safetyReport.requiresHumanApproval,
                createdAt = System.currentTimeMillis(),
            ),
        )
    }

    suspend fun saveRecommendations(recommendations: List<FutureRecommendation>) {
        agiDao.upsertFutureRecommendations(
            recommendations.map {
                FutureRecommendationEntity(
                    recommendationId = it.recommendationId,
                    learnerId = it.learnerId,
                    title = it.title,
                    rationale = it.rationale,
                    priority = it.priority,
                    generatedAt = it.generatedAt,
                )
            },
        )
    }

    suspend fun saveKnowledgeEvolution(proposal: KnowledgeEvolutionProposal) {
        agiDao.upsertKnowledgeEvolution(
            KnowledgeEvolutionEntity(
                proposalId = proposal.proposalId,
                institutionId = proposal.institutionId,
                topic = proposal.topic,
                missingConcepts = proposal.missingConcepts,
                curriculumUpdates = proposal.curriculumUpdates,
                requiredApprovalRole = proposal.requiredApprovalRole.name,
                status = proposal.status.name,
            ),
        )
    }

    suspend fun saveGoal(memory: LearnerMemory, goalPlanner: GoalPlanner) {
        agiDao.upsertGoal(goalPlanner.nextGoal(memory).toEntity())
    }

    fun observeDashboard(institutionId: String): Flow<AGIDashboardState> =
        combine(
            agiDao.observeRecentDecisions(),
            agiDao.observeKnowledgeEvolution(institutionId),
        ) { decisions, evolution ->
            AGIDashboardState(
                activeAgents = decisions.flatMap { decision ->
                    decision.selectedAgents.mapNotNull { name ->
                        runCatching { com.rola.app.domain.model.AGIAgentRole.valueOf(name) }.getOrNull()
                    }
                }.distinct(),
                recentDecisions = emptyList(),
                cognitiveReports = emptyList(),
                curriculumEvolution = evolution.map {
                    KnowledgeEvolutionProposal(
                        proposalId = it.proposalId,
                        institutionId = it.institutionId,
                        topic = it.topic,
                        missingConcepts = it.missingConcepts,
                        curriculumUpdates = it.curriculumUpdates,
                        status = runCatching { TeacherApprovalStatus.valueOf(it.status) }.getOrDefault(TeacherApprovalStatus.PendingReview),
                    )
                },
                knowledgeGrowth = evolution.map { "${it.topic}: ${it.missingConcepts.size} proposed concepts." },
                safetyAlerts = decisions.filter { it.requiresHumanApproval }.map { "${it.actionTitle} needs human approval." },
            )
        }
}
