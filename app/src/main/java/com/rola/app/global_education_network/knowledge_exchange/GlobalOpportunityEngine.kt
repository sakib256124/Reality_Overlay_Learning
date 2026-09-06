package com.rola.app.global_education_network.knowledge_exchange

import com.rola.app.global_education_network.network_core.GlobalEducationNetworkRequest
import com.rola.app.global_education_network.network_core.GlobalOpportunityPlan
import javax.inject.Inject

class GlobalOpportunityEngine @Inject constructor() {
    fun recommend(request: GlobalEducationNetworkRequest): GlobalOpportunityPlan =
        GlobalOpportunityPlan(
            opportunityId = "opportunity-${request.userId}",
            courses = request.goals.map { "global course for $it" },
            scholarships = listOf("STEM access scholarship", "research learner grant"),
            researchOpportunities = listOf("AI education study", "cross-language learning research"),
            globalProjects = listOf("climate learning project", "robotics collaboration"),
            learningCommunities = listOf("worldwide AI learners", "international research classroom"),
        )
}
