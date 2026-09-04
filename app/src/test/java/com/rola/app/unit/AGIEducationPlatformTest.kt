package com.rola.app.unit

import com.rola.app.core.agi.AISafetySystem
import com.rola.app.core.agi.CognitiveEngine
import com.rola.app.core.agi.GoalPlanner
import com.rola.app.core.agi.MemoryManager
import com.rola.app.core.agi.ReasoningEngine
import com.rola.app.core.agi.SimulationLearningEnvironment
import com.rola.app.core.agi.agents.AssessmentAgent
import com.rola.app.core.agi.agents.KnowledgeExpansionAgent
import com.rola.app.core.agi.agents.PersonalMentorAgent
import com.rola.app.core.agi.agents.RecommendationAgent
import com.rola.app.core.agi.agents.TeachingAgent
import com.rola.app.core.agi.AutonomousAgentManager
import com.rola.app.domain.model.AGIAgentRole
import com.rola.app.domain.model.AGIActivityType
import com.rola.app.domain.model.AGILearningActionType
import com.rola.app.domain.model.AGILearningEvent
import com.rola.app.domain.model.AGIPrivacyMode
import com.rola.app.domain.model.SimulationScenarioType
import com.rola.app.domain.model.SkillLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AGIEducationPlatformTest {
    private val memoryManager = MemoryManager()
    private val cognitiveEngine = CognitiveEngine()
    private val reasoningEngine = ReasoningEngine()
    private val goalPlanner = GoalPlanner()
    private val safetySystem = AISafetySystem()
    private val simulationEnvironment = SimulationLearningEnvironment()
    private val agentManager = AutonomousAgentManager(
        teachingAgent = TeachingAgent(),
        researchAgent = com.rola.app.core.agi.agents.ResearchAgent(),
        assessmentAgent = AssessmentAgent(),
        recommendationAgent = RecommendationAgent(),
        knowledgeExpansionAgent = KnowledgeExpansionAgent(),
        personalMentorAgent = PersonalMentorAgent(),
    )

    @Test
    fun memorySystemStoresMistakesSkillsInterestsAndPatterns() {
        val memory = memoryManager.buildMemory(
            learnerId = "learner-1",
            history = listOf(
                AGILearningEvent("event-1", "learner-1", AGIActivityType.ObjectScan, "Circuits", "Scanned wire", 80),
                AGILearningEvent("event-2", "learner-1", AGIActivityType.QuizAttempt, "Resistance", "Missed formula", 45),
                AGILearningEvent("event-3", "learner-1", AGIActivityType.ARExperiment, "Circuits", "Built virtual circuit", 75),
            ),
        )

        assertEquals(SkillLevel.Intermediate, memory.knowledgeLevel)
        assertTrue(memory.previousMistakes.contains("Resistance"))
        assertTrue(memory.skills.any { it.name == "Circuits" })
        assertTrue(memory.learningPatterns.any { it.contains("spatial", ignoreCase = true) })
    }

    @Test
    fun agentManagerSelectsAssessmentAndRecommendationForQuiz() {
        val event = AGILearningEvent("event-1", "learner-1", AGIActivityType.QuizAttempt, "Energy", "Low score", 52)
        val selected = agentManager.selectAgents(event)

        assertTrue(AGIAgentRole.AssessmentAgent in selected)
        assertTrue(AGIAgentRole.RecommendationAgent in selected)
        assertFalse(AGIAgentRole.ResearchAgent in selected)
    }

    @Test
    fun reasoningChoosesAssessmentActionForWeakQuizSignal() {
        val memory = memoryManager.buildMemory(
            learnerId = "learner-1",
            history = listOf(AGILearningEvent("event-1", "learner-1", AGIActivityType.QuizAttempt, "Energy", "Low score", 52)),
        )

        val action = reasoningEngine.selectLearningAction(memory, listOf(AGIAgentRole.AssessmentAgent), "Energy")

        assertEquals(AGILearningActionType.GenerateAssessment, action.actionType)
        assertTrue(action.description.contains("diagnostic", ignoreCase = true))
    }

    @Test
    fun cognitiveEnginePredictsReteachingForLowRetention() {
        val memory = memoryManager.buildMemory(
            learnerId = "learner-1",
            history = listOf(
                AGILearningEvent("event-1", "learner-1", AGIActivityType.QuizAttempt, "Atoms", "Incorrect model", 40),
                AGILearningEvent("event-2", "learner-1", AGIActivityType.QuizAttempt, "Atoms", "Still confused", 50),
            ),
        )

        val report = cognitiveEngine.analyze(memory)

        assertEquals(SkillLevel.Beginner, report.conceptDifficulty)
        assertTrue(report.futurePrediction.contains("struggle", ignoreCase = true))
    }

    @Test
    fun safetySystemRequiresHumanApprovalForWeakEvidenceOrPrivacyRisk() {
        val report = safetySystem.verify(
            content = "Email this learner and all students always need the same plan.",
            evidence = emptyList(),
            privacyMode = AGIPrivacyMode.Strict,
        )

        assertFalse(report.verified)
        assertTrue(report.requiresHumanApproval)
    }

    @Test
    fun simulationEnvironmentCreatesARLaboratoryScenario() {
        val scenario = simulationEnvironment.createScenario("Energy transfer", SimulationScenarioType.ARLaboratory)

        assertEquals(SimulationScenarioType.ARLaboratory, scenario.scenarioType)
        assertTrue(scenario.instructions.any { it.contains("Scan", ignoreCase = true) })
        assertTrue(scenario.expectedLearningSignals.isNotEmpty())
    }

    @Test
    fun goalPlannerTargetsPreviousMistakesFirst() {
        val memory = memoryManager.buildMemory(
            learnerId = "learner-1",
            history = listOf(AGILearningEvent("event-1", "learner-1", AGIActivityType.QuizAttempt, "Gravity", "Missed force direction", 45)),
        )

        val goal = goalPlanner.nextGoal(memory)

        assertEquals("Gravity", goal.targetSkill)
        assertTrue(goal.title.contains("Gravity"))
    }
}
