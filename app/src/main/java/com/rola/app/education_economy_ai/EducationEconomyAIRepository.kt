package com.rola.app.education_economy_ai

import com.rola.app.data.database.EducationEconomyAIDao
import com.rola.app.data.database.entities.EducationEconomyAnalyticsEntity
import com.rola.app.data.database.entities.EducationEconomyCreatorProfileEntity
import com.rola.app.data.database.entities.EducationEconomyDigitalLearningAssetEntity
import com.rola.app.data.database.entities.EducationEconomyInnovationEntity
import com.rola.app.data.database.entities.EducationEconomyLearningValueScoreEntity
import com.rola.app.data.database.entities.EducationEconomyReputationRecordEntity
import com.rola.app.data.database.entities.EducationEconomySkillCertificateEntity
import com.rola.app.data.database.entities.EducationEconomyTransactionEntity
import com.rola.app.education_economy_ai.economy_core.EducationEconomyResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class EducationEconomyAIRepository @Inject constructor(private val dao: EducationEconomyAIDao) {
    fun observeDashboard(): Flow<EducationEconomyDashboardState> =
        combine(
            dao.observeAsset(),
            dao.observeCreator(),
            dao.observeInnovation(),
            dao.observeCertificate(),
            dao.observeValue(),
            dao.observeTransaction(),
            dao.observeReputation(),
            dao.observeAnalytics(),
        ) { values ->
            val asset = values[0] as EducationEconomyDigitalLearningAssetEntity?
            val creator = values[1] as EducationEconomyCreatorProfileEntity?
            val innovation = values[2] as EducationEconomyInnovationEntity?
            val certificate = values[3] as EducationEconomySkillCertificateEntity?
            val value = values[4] as EducationEconomyLearningValueScoreEntity?
            val transaction = values[5] as EducationEconomyTransactionEntity?
            val reputation = values[6] as EducationEconomyReputationRecordEntity?
            val analytics = values[7] as EducationEconomyAnalyticsEntity?
            EducationEconomyDashboardState(
                digitalAssets = asset?.assets.orEmpty() + asset?.distribution.orEmpty(),
                creatorActivity = analytics?.creatorActivity.orEmpty() + creator?.aiCreators.orEmpty(),
                innovationTrends = innovation?.learningModels.orEmpty() + innovation?.educationTechnologies.orEmpty(),
                learningValue = value?.educationalEffectiveness ?: 0,
                economyScore = analytics?.economyScore ?: 0,
                certifications = certificate?.verifiedCertificates.orEmpty() + certificate?.achievements.orEmpty(),
                reputation = listOfNotNull(reputation?.institutionRanking, "Creator ${reputation?.creatorReputation ?: 0}%", "AI ${reputation?.aiContributionScore ?: 0}%"),
                resourcePerformance = analytics?.resourcePerformance.orEmpty(),
                governanceStatus = analytics?.governanceStatus.orEmpty(),
                transactionStatus = if (transaction?.verifiedExchange == true) "Verified exchange and data privacy active." else "",
            )
        }

    suspend fun save(result: EducationEconomyResult, learnerId: String) {
        val governance = result.governance
        val governanceStatus = "Asset verification ${governance.assetVerification}, creator auth ${governance.creatorAuthentication}, certificate security ${governance.certificateSecurity}, privacy ${governance.dataPrivacy}, transparent evaluation ${governance.transparentEvaluation}."
        dao.upsertAsset(EducationEconomyDigitalLearningAssetEntity(result.assets.assetId, result.assets.assets, result.assets.organization, result.assets.verification, result.assets.distribution))
        dao.upsertCreator(EducationEconomyCreatorProfileEntity(result.creatorProfile.creatorId, result.creatorProfile.teachers, result.creatorProfile.researchers, result.creatorProfile.developers, result.creatorProfile.aiCreators, result.creatorProfile.organizations, result.creatorProfile.reputationBuilding))
        dao.upsertInnovation(EducationEconomyInnovationEntity(result.innovation.innovationId, result.innovation.learningModels, result.innovation.teachingApproaches, result.innovation.educationTechnologies, result.innovation.aiLearningMethods))
        dao.upsertCertificate(EducationEconomySkillCertificateEntity(result.certification.certificateId, result.certification.verifiedCertificates, result.certification.skillProfiles, result.certification.competencyRecords, result.certification.achievements, result.certification.certificateSecure))
        dao.upsertValue(EducationEconomyLearningValueScoreEntity(result.valueScore.valueId, result.valueScore.educationalEffectiveness, result.valueScore.skillImprovement, result.valueScore.knowledgeImpact, result.valueScore.learnerOutcomes, result.valueScore.transparentEvaluation))
        dao.upsertTransaction(EducationEconomyTransactionEntity("transaction-${result.resultId}", learnerId, result.assets.assetId, result.market.marketId, verifiedExchange = true, dataPrivacy = result.governance.dataPrivacy))
        dao.upsertReputation(EducationEconomyReputationRecordEntity(result.reputation.reputationId, result.reputation.creatorReputation, result.reputation.learnerAchievements, result.reputation.institutionRanking, result.reputation.aiContributionScore))
        dao.upsertAnalytics(EducationEconomyAnalyticsEntity(result.analytics.analyticsId, result.analytics.learningTrends, result.analytics.creatorActivity, result.analytics.resourcePerformance, result.analytics.globalDemand, result.analytics.economyScore, governanceStatus))
    }
}

data class EducationEconomyDashboardState(
    val digitalAssets: List<String> = emptyList(),
    val creatorActivity: List<String> = emptyList(),
    val innovationTrends: List<String> = emptyList(),
    val learningValue: Int = 0,
    val economyScore: Int = 0,
    val certifications: List<String> = emptyList(),
    val reputation: List<String> = emptyList(),
    val resourcePerformance: List<String> = emptyList(),
    val governanceStatus: String = "",
    val transactionStatus: String = "",
)
