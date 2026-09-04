package com.rola.app.core.agi

import com.rola.app.domain.model.AGIAccessContext
import com.rola.app.domain.model.AGILearningActionType
import com.rola.app.domain.model.AGIPermission
import com.rola.app.domain.model.AGIOrchestrationRequest
import com.rola.app.domain.model.AGIOrchestrationResult
import com.rola.app.domain.model.SimulationScenarioType
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AGIOrchestrator @Inject constructor(
    private val agentManager: AutonomousAgentManager,
    private val reasoningEngine: ReasoningEngine,
    private val cognitiveEngine: CognitiveEngine,
    private val goalPlanner: GoalPlanner,
    private val personalLearningMentor: PersonalLearningMentor,
    private val simulationLearningEnvironment: SimulationLearningEnvironment,
    private val safetySystem: AISafetySystem,
    private val agiRepository: AGIRepository,
) {
    suspend fun orchestrate(
        request: AGIOrchestrationRequest,
        accessContext: AGIAccessContext,
    ): AGIOrchestrationResult {
        requireAccess(accessContext, AGIPermission.RunAgents)
        require(accessContext.institutionId == request.institutionId) { "Institution scope mismatch." }

        val memory = agiRepository.loadMemory(request.learnerId, request.event)
        val agentMessages = agentManager.runAgents(request.event, memory)
        val selectedAgents = agentMessages.map { it.agent }.distinct()
        val reasoningTrace = reasoningEngine.reason(memory, request.objective, selectedAgents) +
            agentMessages.map { "${it.agent.name}: ${it.intent}" } +
            personalLearningMentor.dailyGuidance(memory).take(1)
        val action = reasoningEngine.selectLearningAction(memory, selectedAgents, request.event.topic)
            .let { selected ->
                if (request.event.activityType.name.contains("AR") && selected.actionType != AGILearningActionType.GenerateAssessment) {
                    val scenario = simulationLearningEnvironment.createScenario(request.event.topic, SimulationScenarioType.ARLaboratory)
                    selected.copy(
                        actionType = AGILearningActionType.RunSimulation,
                        title = scenario.title,
                        description = scenario.instructions.joinToString(" "),
                    )
                } else {
                    selected
                }
            }
        val cognitiveReport = cognitiveEngine.analyze(memory)
        val recommendations = goalPlanner.futureRecommendations(memory)
        val safetyReport = safetySystem.verify(
            content = action.description,
            evidence = agentMessages.flatMap { it.evidence } + reasoningTrace,
            privacyMode = request.privacyMode,
        )
        val result = AGIOrchestrationResult(
            decisionId = "agi-decision-${UUID.randomUUID()}",
            selectedAgents = selectedAgents,
            reasoningTrace = reasoningTrace,
            learningAction = action,
            safetyReport = safetyReport,
            cognitiveReport = cognitiveReport,
            recommendations = recommendations,
        )
        agiRepository.saveMemory(memory, request.event)
        agiRepository.saveRecommendations(recommendations)
        agiRepository.saveGoal(memory, goalPlanner)
        agiRepository.saveDecision(request.institutionId, request.learnerId, result)
        return result
    }

    fun communicationPlan(request: AGIOrchestrationRequest): String =
        agentManager.communicationPlan(request.event)

    private fun requireAccess(
        accessContext: AGIAccessContext,
        permission: AGIPermission,
    ) {
        require(permission in accessContext.permissions) { "Missing AGI permission: ${permission.name}" }
    }
}
