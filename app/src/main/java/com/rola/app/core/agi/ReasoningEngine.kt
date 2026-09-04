package com.rola.app.core.agi

import com.rola.app.core.agi.memory.LearnerMemory
import com.rola.app.domain.model.AGIAgentRole
import com.rola.app.domain.model.AGILearningAction
import com.rola.app.domain.model.AGILearningActionType
import com.rola.app.domain.model.RecommendationPriority
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReasoningEngine @Inject constructor() {
    fun reason(
        memory: LearnerMemory,
        objective: String,
        selectedAgents: List<AGIAgentRole>,
    ): List<String> = buildList {
        add("Objective: $objective")
        add("Knowledge level: ${memory.knowledgeLevel.name}")
        add("Learning speed: ${memory.learningSpeed.name}")
        if (memory.previousMistakes.isNotEmpty()) {
            add("Prior mistakes indicate gaps in ${memory.previousMistakes.take(3).joinToString()}.")
        }
        if (memory.interests.isNotEmpty()) {
            add("Use interests as anchors: ${memory.interests.take(3).joinToString()}.")
        }
        add("Agent route: ${selectedAgents.joinToString(" -> ") { it.name }}")
    }

    fun selectLearningAction(
        memory: LearnerMemory,
        selectedAgents: List<AGIAgentRole>,
        topic: String,
    ): AGILearningAction {
        val primaryAgent = selectedAgents.firstOrNull() ?: AGIAgentRole.PersonalMentorAgent
        val actionType = when {
            AGIAgentRole.AssessmentAgent in selectedAgents -> AGILearningActionType.GenerateAssessment
            AGIAgentRole.KnowledgeExpansionAgent in selectedAgents -> AGILearningActionType.ExpandKnowledge
            AGIAgentRole.ResearchAgent in selectedAgents -> AGILearningActionType.UpdateCurriculum
            AGIAgentRole.TeachingAgent in selectedAgents -> AGILearningActionType.TeachConcept
            else -> AGILearningActionType.MentorCheckIn
        }
        val priority = when {
            memory.retentionScore < 55 -> RecommendationPriority.High
            memory.previousMistakes.contains(topic) -> RecommendationPriority.High
            memory.learningSpeed.name == "Fast" -> RecommendationPriority.Medium
            else -> RecommendationPriority.Medium
        }
        return AGILearningAction(
            actionId = "agi-action-${UUID.randomUUID()}",
            title = titleFor(actionType, topic),
            description = explanationFor(actionType, topic, memory),
            agentRole = primaryAgent,
            actionType = actionType,
            priority = priority,
        )
    }

    fun connectConcepts(
        topic: String,
        knownConcepts: List<String>,
    ): List<String> = knownConcepts
        .filterNot { it.equals(topic, ignoreCase = true) }
        .take(5)
        .map { "$topic connects to $it through evidence, transfer, or prerequisite knowledge." }
        .ifEmpty { listOf("$topic needs a prerequisite map before advanced reasoning.") }

    private fun titleFor(
        actionType: AGILearningActionType,
        topic: String,
    ): String = when (actionType) {
        AGILearningActionType.TeachConcept -> "Teach $topic with adaptive explanation"
        AGILearningActionType.GenerateAssessment -> "Assess understanding of $topic"
        AGILearningActionType.RecommendPractice -> "Practice $topic"
        AGILearningActionType.ExpandKnowledge -> "Expand knowledge graph for $topic"
        AGILearningActionType.UpdateCurriculum -> "Update curriculum around $topic"
        AGILearningActionType.MentorCheckIn -> "Mentor check-in for $topic"
        AGILearningActionType.RunSimulation -> "Run simulation for $topic"
    }

    private fun explanationFor(
        actionType: AGILearningActionType,
        topic: String,
        memory: LearnerMemory,
    ): String = when (actionType) {
        AGILearningActionType.TeachConcept -> "Generate an explanation of $topic at ${memory.knowledgeLevel.name} level with examples from learner interests."
        AGILearningActionType.GenerateAssessment -> "Create diagnostic questions for $topic and compare results to previous mistakes."
        AGILearningActionType.RecommendPractice -> "Schedule spaced practice for $topic based on retention score ${memory.retentionScore}."
        AGILearningActionType.ExpandKnowledge -> "Add missing relationships for $topic to the knowledge intelligence layer."
        AGILearningActionType.UpdateCurriculum -> "Propose teacher-reviewed lesson improvements for $topic."
        AGILearningActionType.MentorCheckIn -> "Guide the learner toward one small goal for $topic today."
        AGILearningActionType.RunSimulation -> "Use an AR lab or virtual experiment to test $topic interactively."
    }
}
