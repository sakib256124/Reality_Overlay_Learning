package com.rola.app.global_education_network.knowledge_exchange

import com.rola.app.global_education_network.network_core.KnowledgeExchangePlan
import javax.inject.Inject

class KnowledgeExchangeNetwork @Inject constructor() {
    fun exchange(): KnowledgeExchangePlan =
        KnowledgeExchangePlan(
            exchangeId = "exchange-global",
            courses = listOf("AI ethics course", "global STEM course"),
            research = listOf("open research digest", "teacher research brief"),
            educationalResources = listOf("AR lab resource", "translated simulation"),
            learningStrategies = listOf("project-based collaboration", "peer tutoring"),
            innovations = listOf("international micro-credential", "AI-supported course exchange"),
        )
}
