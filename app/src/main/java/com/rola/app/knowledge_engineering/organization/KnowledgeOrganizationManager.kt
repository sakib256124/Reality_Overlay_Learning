package com.rola.app.knowledge_engineering.organization

import com.rola.app.knowledge_engineering.knowledge_core.KnowledgeOrganization
import com.rola.app.knowledge_engineering.knowledge_core.StructuredKnowledge
import javax.inject.Inject

class KnowledgeOrganizationManager @Inject constructor() {
    fun organize(knowledge: StructuredKnowledge): KnowledgeOrganization =
        KnowledgeOrganization(
            mappingId = "mapping-${knowledge.entityId}",
            conceptMappings = knowledge.definitions + knowledge.examples,
            learningPathways = listOf("beginner explanation", "practice", "advanced reasoning", "research connection"),
            semanticIndexes = knowledge.definitions.map { "semantic:$it" },
        )
}
