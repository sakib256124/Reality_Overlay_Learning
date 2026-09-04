package com.rola.app.cognitive_ai

import com.rola.app.cognitive_ai.brain.CognitiveLearningResult
import com.rola.app.cognitive_ai.brain.LearningBrain
import com.rola.app.domain.model.CognitiveDashboardState
import com.rola.app.domain.model.CognitiveLearningActivity
import com.rola.app.domain.model.CognitivePermission
import com.rola.app.domain.model.CognitiveSecurityContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CognitiveEngine @Inject constructor(
    private val learningBrain: LearningBrain,
) {
    fun analyzeStudent(
        userId: String,
        activities: List<CognitiveLearningActivity>,
        securityContext: CognitiveSecurityContext,
    ): CognitiveLearningResult = learningBrain.analyze(userId, activities, securityContext)

    fun dashboardFor(result: CognitiveLearningResult): CognitiveDashboardState =
        learningBrain.dashboard(result)

    fun sampleDashboard(
        userId: String,
        securityContext: CognitiveSecurityContext,
    ): CognitiveDashboardState {
        require(CognitivePermission.ViewCognitiveProfile in securityContext.permissions) {
            "Missing cognitive permission: ViewCognitiveProfile"
        }
        return dashboardFor(analyzeStudent(userId, learningBrain.sampleActivities(userId), securityContext))
    }
}
