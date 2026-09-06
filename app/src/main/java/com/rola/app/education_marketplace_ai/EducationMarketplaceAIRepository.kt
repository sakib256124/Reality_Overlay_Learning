package com.rola.app.education_marketplace_ai

import com.rola.app.data.database.EducationMarketplaceAIDao
import com.rola.app.data.database.entities.EducationMarketplaceCourseModelEntity
import com.rola.app.data.database.entities.EducationMarketplaceCreatorEntity
import com.rola.app.data.database.entities.EducationMarketplaceLearningMaterialEntity
import com.rola.app.data.database.entities.EducationMarketplaceRecommendationEntity
import com.rola.app.data.database.entities.EducationMarketplaceResourceAnalyticsEntity
import com.rola.app.data.database.entities.EducationMarketplaceResourceEntity
import com.rola.app.data.database.entities.EducationMarketplaceResourceRatingEntity
import com.rola.app.data.database.entities.EducationMarketplaceTransactionEntity
import com.rola.app.education_marketplace_ai.marketplace_core.EducationMarketplaceResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class EducationMarketplaceAIRepository @Inject constructor(private val dao: EducationMarketplaceAIDao) {
    fun observeDashboard(): Flow<EducationMarketplaceDashboardState> =
        combine(
            dao.observeResource(),
            dao.observeCreator(),
            dao.observeRating(),
            dao.observeRecommendation(),
            dao.observeCourse(),
            dao.observeMaterial(),
            dao.observeAnalytics(),
            dao.observeTransaction(),
        ) { values ->
            val resource = values[0] as EducationMarketplaceResourceEntity?
            val creator = values[1] as EducationMarketplaceCreatorEntity?
            val rating = values[2] as EducationMarketplaceResourceRatingEntity?
            val recommendation = values[3] as EducationMarketplaceRecommendationEntity?
            val course = values[4] as EducationMarketplaceCourseModelEntity?
            val material = values[5] as EducationMarketplaceLearningMaterialEntity?
            val analytics = values[6] as EducationMarketplaceResourceAnalyticsEntity?
            val transaction = values[7] as EducationMarketplaceTransactionEntity?
            EducationMarketplaceDashboardState(
                recommendedResources = recommendation?.bestResources.orEmpty() + recommendation?.bestResearchMaterials.orEmpty(),
                trendingCourses = recommendation?.bestCourses.orEmpty() + material?.digitalCourses.orEmpty(),
                aiGeneratedContent = course?.modules.orEmpty() + course?.projects.orEmpty(),
                creatorActivity = creator?.teachers.orEmpty() + creator?.aiCreators.orEmpty(),
                learningAnalytics = analytics?.learningEffectiveness ?: 0,
                resourceQuality = rating?.educationalValue ?: 0,
                marketplaceResources = resource?.resources.orEmpty(),
                globalTrends = analytics?.globalTrends.orEmpty(),
                trustStatus = analytics?.trustStatus.orEmpty(),
                transactionStatus = if (transaction?.secureTransaction == true) "Secure transaction, copyright protection, and privacy active." else "",
            )
        }

    suspend fun save(result: EducationMarketplaceResult, learnerId: String) {
        dao.upsertResource(EducationMarketplaceResourceEntity(result.discovery.discoveryId, result.discovery.resources, result.discovery.classifications, result.discovery.searchSignals, result.discovery.personalizedMatches, result.status.name))
        dao.upsertCreator(EducationMarketplaceCreatorEntity(result.creatorNetwork.creatorId, result.creatorNetwork.teachers, result.creatorNetwork.researchers, result.creatorNetwork.universities, result.creatorNetwork.aiCreators, result.creatorNetwork.organizations, result.creatorNetwork.publishingEnabled))
        dao.upsertRating(EducationMarketplaceResourceRatingEntity(result.quality.qualityId, result.quality.accuracyScore, result.quality.educationalValue, result.quality.difficultyLevel, result.quality.engagementQuality, result.quality.scientificReliability, result.quality.validationRequired))
        dao.upsertRecommendation(EducationMarketplaceRecommendationEntity(result.recommendations.recommendationId, result.recommendations.bestCourses, result.recommendations.bestResources, result.recommendations.bestProjects, result.recommendations.bestResearchMaterials, result.recommendations.rankingReason))
        dao.upsertCourse(EducationMarketplaceCourseModelEntity(result.course.courseId, result.course.modules, result.course.assignments, result.course.assessments, result.course.projects, result.course.integratedSystems))
        dao.upsertMaterial(EducationMarketplaceLearningMaterialEntity(result.catalog.catalogId, result.catalog.digitalCourses, result.catalog.lessons, result.catalog.videos, result.catalog.documents, result.catalog.arExperiences, result.catalog.virtualLabs, result.catalog.researchContent, result.catalog.versionControl, result.catalog.accessibilityReady))
        dao.upsertAnalytics(EducationMarketplaceResourceAnalyticsEntity(result.analytics.analyticsId, result.analytics.resourcePopularity, result.analytics.learningEffectiveness, result.analytics.studentOutcomes, result.analytics.globalTrends, result.analytics.trustStatus))
        dao.upsertTransaction(EducationMarketplaceTransactionEntity("transaction-${result.resultId}", learnerId, result.discovery.discoveryId, copyrightProtected = true, userPrivacyProtected = true, secureTransaction = true))
    }
}

data class EducationMarketplaceDashboardState(
    val recommendedResources: List<String> = emptyList(),
    val trendingCourses: List<String> = emptyList(),
    val aiGeneratedContent: List<String> = emptyList(),
    val creatorActivity: List<String> = emptyList(),
    val learningAnalytics: Int = 0,
    val resourceQuality: Int = 0,
    val marketplaceResources: List<String> = emptyList(),
    val globalTrends: List<String> = emptyList(),
    val trustStatus: String = "",
    val transactionStatus: String = "",
)
