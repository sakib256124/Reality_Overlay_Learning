package com.rola.app.unit

import com.rola.app.agi_network.agents.AutonomousAgentCoordinator
import com.rola.app.agi_network.agents.MultiAgentEducationSystem
import com.rola.app.agi_network.collaboration.AgentCommunicationManager
import com.rola.app.agi_network.collaboration.GlobalAIIntelligenceNetwork
import com.rola.app.agi_network.decision.AGIDecisionEngine
import com.rola.app.agi_network.evolution.AIImprovementEngine
import com.rola.app.agi_network.evolution.EvolutionaryCurriculumEngine
import com.rola.app.agi_network.evolution.SelfLearningManager
import com.rola.app.agi_network.governance.AGIGovernanceManager
import com.rola.app.agi_network.intelligence.AGINetworkAccessContext
import com.rola.app.agi_network.intelligence.AGINetworkAgentRole
import com.rola.app.agi_network.intelligence.AGINetworkEngine
import com.rola.app.agi_network.intelligence.AGINetworkPermission
import com.rola.app.agi_network.intelligence.AGIAnalyticsEngine
import com.rola.app.agi_network.intelligence.AGIReasoningEngine
import com.rola.app.agi_network.intelligence.EducationNetworkSignal
import com.rola.app.agi_network.intelligence.GovernanceDecision
import com.rola.app.agi_network.knowledge.KnowledgeEvolutionEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AGINetworkPlatformTest {
    private val communicationManager = AgentCommunicationManager()
    private val multiAgentSystem = MultiAgentEducationSystem()
    private val coordinator = AutonomousAgentCoordinator(multiAgentSystem, communicationManager)
    private val engine = AGINetworkEngine(
        agentCoordinator = coordinator,
        reasoningEngine = AGIReasoningEngine(),
        selfLearningManager = SelfLearningManager(),
        improvementEngine = AIImprovementEngine(),
        knowledgeEvolutionEngine = KnowledgeEvolutionEngine(),
        decisionEngine = AGIDecisionEngine(),
        curriculumEngine = EvolutionaryCurriculumEngine(),
        analyticsEngine = AGIAnalyticsEngine(),
        globalNetwork = GlobalAIIntelligenceNetwork(),
        governanceManager = AGIGovernanceManager(),
    )

    @Test
    fun multiAgentSystem_selectsAssessmentAndKnowledgeAgentsForWeakResearchSignal() {
        val agents = multiAgentSystem.selectAgents(sampleSignal())

        assertTrue(AGINetworkAgentRole.AssessmentAgent in agents)
        assertTrue(AGINetworkAgentRole.KnowledgeAgent in agents)
        assertTrue(AGINetworkAgentRole.ResearchAgent in agents)
    }

    @Test
    fun agentCommunication_routesEvidenceBetweenSelectedAgents() {
        val plan = coordinator.coordinate(sampleSignal())

        assertTrue(plan.messages.isNotEmpty())
        assertTrue(plan.conflictResolution.contains("review", ignoreCase = true))
    }

    @Test
    fun governance_blocksPublicationWithoutApprovalPermission() {
        val result = engine.runEducationNetworkCycle(
            signal = sampleSignal(),
            accessContext = AGINetworkAccessContext(
                userId = "teacher-1",
                institutionId = "school-1",
                permissions = setOf(AGINetworkPermission.RunAgents),
                humanSupervisorId = "teacher-1",
            ),
        )

        assertEquals(GovernanceDecision.NeedsHumanReview, result.governanceRecord.decision)
        assertTrue(result.governanceRecord.humanApprovalRequired)
    }

    @Test
    fun engine_createsSelfEvolvingDraftDecisionAndAnalytics() {
        val result = engine.runEducationNetworkCycle(
            signal = sampleSignal(),
            accessContext = AGINetworkAccessContext(
                userId = "director-1",
                institutionId = "school-1",
                permissions = setOf(
                    AGINetworkPermission.RunAgents,
                    AGINetworkPermission.ApprovePublication,
                ),
                humanSupervisorId = "director-1",
            ),
        )

        assertTrue(result.educationalDecision.teachingApproach.contains("Simplified", ignoreCase = true))
        assertTrue(result.improvementPlan.requiresOfflineEvaluation)
        assertTrue(result.analyticsReport.knowledgeGrowth.isNotEmpty())
        assertEquals(GovernanceDecision.ApprovedForDraft, result.governanceRecord.decision)
    }

    private fun sampleSignal(): EducationNetworkSignal =
        EducationNetworkSignal(
            signalId = "signal-1",
            learnerId = "learner-1",
            institutionId = "school-1",
            topic = "Fractions",
            activityType = "quiz-and-research",
            learningOutcomeScore = 54,
            contentQualityScore = 68,
            researchEvidence = listOf("Ratio model: add visual bridge", "Area model: use AR tile examples"),
        )
}
