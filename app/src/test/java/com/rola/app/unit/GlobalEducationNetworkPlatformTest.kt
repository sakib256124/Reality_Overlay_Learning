package com.rola.app.unit

import com.rola.app.global_education_network.analytics.GlobalLearningAnalytics
import com.rola.app.global_education_network.collaboration.GlobalCollaborationEngine
import com.rola.app.global_education_network.collaboration.GlobalResearchNetwork
import com.rola.app.global_education_network.communication.GlobalCommunicationAI
import com.rola.app.global_education_network.governance.NetworkGovernanceManager
import com.rola.app.global_education_network.institutions.InstitutionConnectionManager
import com.rola.app.global_education_network.knowledge_exchange.GlobalOpportunityEngine
import com.rola.app.global_education_network.knowledge_exchange.KnowledgeExchangeNetwork
import com.rola.app.global_education_network.network_core.EducationNetworkManager
import com.rola.app.global_education_network.network_core.GlobalEducationIdentityManager
import com.rola.app.global_education_network.network_core.GlobalEducationNetworkEngine
import com.rola.app.global_education_network.network_core.GlobalEducationNetworkRequest
import com.rola.app.global_education_network.network_core.GlobalNetworkStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GlobalEducationNetworkPlatformTest {
    private val engine = GlobalEducationNetworkEngine(
        GlobalEducationIdentityManager(),
        EducationNetworkManager(),
        InstitutionConnectionManager(),
        GlobalCollaborationEngine(),
        KnowledgeExchangeNetwork(),
        GlobalOpportunityEngine(),
        GlobalCommunicationAI(),
        GlobalResearchNetwork(),
        GlobalLearningAnalytics(),
        NetworkGovernanceManager(),
    )

    @Test
    fun globalNetwork_connectsInstitutionsCollaborationExchangeCommunicationAndGovernance() {
        val result = engine.connectWorld(
            GlobalEducationNetworkRequest(
                userId = "global-learner",
                region = "South Asia",
                goals = listOf("AI collaboration", "robotics research"),
                skills = listOf("programming", "research methods"),
                interests = listOf("global projects", "translated learning"),
                learningHistory = listOf("knowledge discovery", "education marketplace"),
                preferredLanguage = "Bangla",
            ),
        )

        assertEquals(GlobalNetworkStatus.Active, result.status)
        assertTrue(result.identity.verified)
        assertTrue(result.network.globalSync)
        assertTrue(result.institutions.universities.contains("global open university"))
        assertTrue(result.collaboration.aiAgentCollaboration.contains("translation agent"))
        assertTrue(result.exchange.innovations.contains("international micro-credential"))
        assertTrue(result.opportunities.scholarships.isNotEmpty())
        assertTrue(result.communication.realTimeTranslation)
        assertTrue(result.researchNetwork.aiScientists.contains("Knowledge Discovery AI"))
        assertTrue(result.analytics.globalKnowledgeGrowth >= 90)
        assertTrue(result.governance.dataProtection)
    }
}
