package com.rola.app.education_marketplace_ai.marketplace_core

enum class MarketplaceResourceType { Course, Book, ResearchMaterial, Simulation, Project, LearningTool, Dataset }
enum class MarketplaceStatus { Discovering, Recommended, Published, NeedsValidation }

data class EducationMarketplaceRequest(
    val learnerId: String,
    val learningGoal: String,
    val skillLevel: String,
    val cognitiveProfile: String,
    val emotionalState: String,
    val learningHistory: List<String>,
)

data class MarketplaceResourceDiscovery(val discoveryId: String, val resources: List<String>, val classifications: List<String>, val searchSignals: List<String>, val personalizedMatches: List<String>)
data class LearningResourceCatalog(val catalogId: String, val digitalCourses: List<String>, val lessons: List<String>, val videos: List<String>, val documents: List<String>, val arExperiences: List<String>, val virtualLabs: List<String>, val researchContent: List<String>, val versionControl: Boolean, val accessibilityReady: Boolean)
data class ResourceRecommendationPlan(val recommendationId: String, val bestCourses: List<String>, val bestResources: List<String>, val bestProjects: List<String>, val bestResearchMaterials: List<String>, val rankingReason: String)
data class CreatorNetworkState(val creatorId: String, val teachers: List<String>, val researchers: List<String>, val universities: List<String>, val aiCreators: List<String>, val organizations: List<String>, val publishingEnabled: Boolean)
data class ResourceQualityReport(val qualityId: String, val accuracyScore: Int, val educationalValue: Int, val difficultyLevel: String, val engagementQuality: Int, val scientificReliability: Int, val validationRequired: Boolean)
data class AdaptiveResourcePlan(val adaptationId: String, val beginnerVersion: String, val expertVersion: String, val learningStyleFit: String, val masteryAdjustment: String, val speedAdjustment: String)
data class AICourseModel(val courseId: String, val modules: List<String>, val assignments: List<String>, val assessments: List<String>, val projects: List<String>, val integratedSystems: List<String>)
data class MarketplaceIntelligenceReport(val analyticsId: String, val resourcePopularity: List<String>, val learningEffectiveness: Int, val studentOutcomes: List<String>, val globalTrends: List<String>, val trustStatus: String)
data class EducationMarketplaceResult(
    val resultId: String,
    val discovery: MarketplaceResourceDiscovery,
    val catalog: LearningResourceCatalog,
    val recommendations: ResourceRecommendationPlan,
    val creatorNetwork: CreatorNetworkState,
    val quality: ResourceQualityReport,
    val adaptation: AdaptiveResourcePlan,
    val course: AICourseModel,
    val analytics: MarketplaceIntelligenceReport,
    val status: MarketplaceStatus,
)
