package com.rola.app.virtual_campus_ai.intelligence

import com.rola.app.virtual_campus_ai.campus_core.CampusAssistantResponse
import com.rola.app.virtual_campus_ai.campus_core.VirtualCampusProfile
import com.rola.app.virtual_campus_ai.campus_core.VirtualCampusRequest
import javax.inject.Inject

class CampusAssistantAgent @Inject constructor() {
    fun guide(request: VirtualCampusRequest, campus: VirtualCampusProfile): CampusAssistantResponse =
        CampusAssistantResponse(
            assistantId = "campus-assistant-${request.learnerId}",
            navigationGuidance = listOf("go to ${campus.laboratories.first()}", "visit ${campus.libraries.first()} for resources"),
            courseRecommendations = listOf("${request.courseTopic} lab sequence", "research methods seminar"),
            resourceDiscovery = campus.researchCenters + campus.libraries,
            transparentReasoning = "Matched ${request.learningGoal} with available laboratories, research centers, and course context.",
        )
}
