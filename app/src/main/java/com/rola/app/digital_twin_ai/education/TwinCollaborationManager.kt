package com.rola.app.digital_twin_ai.education

import com.rola.app.digital_twin_ai.twin_core.DigitalTwinRequest
import com.rola.app.digital_twin_ai.twin_core.TwinCollaborationSpace
import javax.inject.Inject

class TwinCollaborationManager @Inject constructor() {
    fun collaborate(request: DigitalTwinRequest): TwinCollaborationSpace =
        TwinCollaborationSpace(
            collaborationId = "collaboration-${request.learnerId}",
            students = listOf(request.learnerId, "peer learners"),
            teachers = listOf("teacher reviewer"),
            researchers = listOf("research mentor"),
            aiAgents = listOf("Twin AI", "Research AI", "Planning AI"),
            sharedExperiments = listOf("shared simulation", "collaborative experiment", "research project"),
        )
}
