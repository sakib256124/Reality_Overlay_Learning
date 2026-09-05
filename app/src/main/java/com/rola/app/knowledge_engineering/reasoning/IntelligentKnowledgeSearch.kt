package com.rola.app.knowledge_engineering.reasoning

import com.rola.app.knowledge_engineering.knowledge_core.KnowledgeOrganization
import javax.inject.Inject

class IntelligentKnowledgeSearch @Inject constructor() {
    fun search(query: String, organization: KnowledgeOrganization): List<String> =
        organization.semanticIndexes.filter { it.contains(query, ignoreCase = true) }.ifEmpty { organization.semanticIndexes.take(3) }
}
