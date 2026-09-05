package com.rola.app.education_singularity.knowledge_universe

import javax.inject.Inject

class UniversalKnowledgeNetwork @Inject constructor() {
    fun roadmap(topic: String): List<String> =
        listOf(
            "Connect global institutions around $topic.",
            "Share verified research and learning strategies.",
            "Publish reusable educational innovation packages.",
        )
}
