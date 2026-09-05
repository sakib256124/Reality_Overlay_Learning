package com.rola.app.knowledge_engineering.evolution

import com.rola.app.knowledge_engineering.knowledge_core.KnowledgeEvolution
import com.rola.app.knowledge_engineering.knowledge_core.KnowledgeOrganization
import javax.inject.Inject

class KnowledgeEvolutionManager @Inject constructor() {
    fun evolve(organization: KnowledgeOrganization): KnowledgeEvolution =
        KnowledgeEvolution("evolution-${organization.mappingId}", listOf("mark low-confidence fragments for review"), listOf("dynamic relationship discovered"), organization.learningPathways.map { "improve $it" })
}
