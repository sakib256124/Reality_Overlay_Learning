package com.rola.app.knowledge_engineering.organization

import com.rola.app.knowledge_engineering.knowledge_core.KnowledgeOrganization
import javax.inject.Inject

class AdvancedKnowledgeGraphManager @Inject constructor() {
    fun expand(organization: KnowledgeOrganization): List<String> = organization.conceptMappings + "research connection" + "skill mapping"
}
