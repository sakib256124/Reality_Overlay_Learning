package com.rola.app.planning_ai.strategy

import com.rola.app.planning_ai.planning_core.LearningGoal
import com.rola.app.planning_ai.planning_core.LearningStrategy
import com.rola.app.planning_ai.planning_core.PlanningRequest
import javax.inject.Inject

class StrategyGenerationEngine @Inject constructor() {
    fun generate(request: PlanningRequest, goal: LearningGoal): LearningStrategy =
        LearningStrategy(
            strategyId = "strategy-${goal.goalId}",
            methods = listOf("adaptive micro-lessons", "retrieval practice", "reasoning-first examples", "project application"),
            studyTechniques = listOf("spaced repetition", "self explanation", "confidence check", "emotion-aware pacing"),
            selectedResources = request.resources.ifEmpty { listOf("ROLA knowledge graph", "AI tutor", "lifelong memory") },
            practiceStrategy = "Practice ${request.availableHoursPerWeek.coerceAtLeast(1)} hours weekly with difficulty progression.",
            assessmentStrategy = "Use short diagnostics, weekly quizzes, and human-approved AI recommendations.",
        )
}
