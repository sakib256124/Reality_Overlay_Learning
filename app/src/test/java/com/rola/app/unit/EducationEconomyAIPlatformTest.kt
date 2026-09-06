package com.rola.app.unit

import com.rola.app.education_economy_ai.analytics.EducationEconomyAnalytics
import com.rola.app.education_economy_ai.creator_system.CreatorEconomyManager
import com.rola.app.education_economy_ai.digital_assets.DigitalLearningAssetManager
import com.rola.app.education_economy_ai.economy_core.EconomyStatus
import com.rola.app.education_economy_ai.economy_core.EducationEconomyEngine
import com.rola.app.education_economy_ai.economy_core.EducationEconomyRequest
import com.rola.app.education_economy_ai.governance.EconomyGovernanceManager
import com.rola.app.education_economy_ai.innovation_market.EducationInnovationEngine
import com.rola.app.education_economy_ai.innovation_market.InnovationMarketManager
import com.rola.app.education_economy_ai.value_management.AISkillCertificationManager
import com.rola.app.education_economy_ai.value_management.EducationReputationManager
import com.rola.app.education_economy_ai.value_management.LearningValueAnalyzer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EducationEconomyAIPlatformTest {
    private val engine = EducationEconomyEngine(
        DigitalLearningAssetManager(),
        CreatorEconomyManager(),
        InnovationMarketManager(),
        LearningValueAnalyzer(),
        AISkillCertificationManager(),
        EducationInnovationEngine(),
        EducationReputationManager(),
        EducationEconomyAnalytics(),
        EconomyGovernanceManager(),
    )

    @Test
    fun educationEconomy_managesAssetsCreatorsValueCertificationInnovationAndGovernance() {
        val result = engine.buildEconomy(
            EducationEconomyRequest(
                learnerId = "economy-learner",
                creatorGoal = "publish trusted AI learning resources",
                skillArea = "applied AI",
                learningEvidence = listOf("simulation project", "adaptive assessment", "portfolio artifact"),
                marketSignals = listOf("global AI course demand", "verified certificate demand"),
            ),
        )

        assertEquals(EconomyStatus.Governed, result.status)
        assertTrue(result.assets.assets.contains("skill certification"))
        assertTrue(result.creatorProfile.aiCreators.contains("Knowledge Discovery AI"))
        assertTrue(result.market.aiTools.contains("simulation generator"))
        assertTrue(result.valueScore.educationalEffectiveness >= 90)
        assertTrue(result.certification.certificateSecure)
        assertTrue(result.certification.verifiedCertificates.any { it.contains("applied AI") })
        assertTrue(result.innovation.educationTechnologies.contains("digital twin simulations"))
        assertTrue(result.reputation.creatorReputation >= 90)
        assertTrue(result.analytics.economyScore >= 90)
        assertTrue(result.governance.assetVerification)
        assertTrue(result.governance.transparentEvaluation)
    }
}
