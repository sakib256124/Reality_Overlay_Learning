package com.rola.app.education_economy_ai.economy_core

import com.rola.app.education_economy_ai.analytics.EducationEconomyAnalytics
import com.rola.app.education_economy_ai.creator_system.CreatorEconomyManager
import com.rola.app.education_economy_ai.digital_assets.DigitalLearningAssetManager
import com.rola.app.education_economy_ai.governance.EconomyGovernanceManager
import com.rola.app.education_economy_ai.innovation_market.EducationInnovationEngine
import com.rola.app.education_economy_ai.innovation_market.InnovationMarketManager
import com.rola.app.education_economy_ai.value_management.AISkillCertificationManager
import com.rola.app.education_economy_ai.value_management.EducationReputationManager
import com.rola.app.education_economy_ai.value_management.LearningValueAnalyzer
import javax.inject.Inject

class EducationEconomyEngine @Inject constructor(
    private val assetManager: DigitalLearningAssetManager,
    private val creatorEconomyManager: CreatorEconomyManager,
    private val marketManager: InnovationMarketManager,
    private val valueAnalyzer: LearningValueAnalyzer,
    private val certificationManager: AISkillCertificationManager,
    private val innovationEngine: EducationInnovationEngine,
    private val reputationManager: EducationReputationManager,
    private val analytics: EducationEconomyAnalytics,
    private val governanceManager: EconomyGovernanceManager,
) {
    fun buildEconomy(request: EducationEconomyRequest): EducationEconomyResult {
        val assets = assetManager.createAssets(request)
        val creator = creatorEconomyManager.connectCreators(request)
        val market = marketManager.manageMarket(request)
        val value = valueAnalyzer.evaluate(assets)
        val certification = certificationManager.certify(request)
        return EducationEconomyResult(
            resultId = "education-economy-${request.learnerId}",
            assets = assets,
            creatorProfile = creator,
            market = market,
            valueScore = value,
            certification = certification,
            innovation = innovationEngine.discover(),
            reputation = reputationManager.build(creator, certification),
            analytics = analytics.analyze(market, value),
            governance = governanceManager.govern(),
            status = EconomyStatus.Governed,
        )
    }
}
