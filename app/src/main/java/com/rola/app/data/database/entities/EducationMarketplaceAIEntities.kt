package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "education_marketplace_resources", indices = [Index(value = ["status"])])
data class EducationMarketplaceResourceEntity(@PrimaryKey val resourceId: String, val resources: List<String>, val classifications: List<String>, val searchSignals: List<String>, val personalizedMatches: List<String>, val status: String)
@Entity(tableName = "education_marketplace_creators", indices = [Index(value = ["publishingEnabled"])])
data class EducationMarketplaceCreatorEntity(@PrimaryKey val creatorId: String, val teachers: List<String>, val researchers: List<String>, val universities: List<String>, val aiCreators: List<String>, val organizations: List<String>, val publishingEnabled: Boolean)
@Entity(tableName = "education_marketplace_resource_ratings", indices = [Index(value = ["accuracyScore"]), Index(value = ["scientificReliability"])])
data class EducationMarketplaceResourceRatingEntity(@PrimaryKey val qualityId: String, val accuracyScore: Int, val educationalValue: Int, val difficultyLevel: String, val engagementQuality: Int, val scientificReliability: Int, val validationRequired: Boolean)
@Entity(tableName = "education_marketplace_recommendations", indices = [Index(value = ["recommendationId"])])
data class EducationMarketplaceRecommendationEntity(@PrimaryKey val recommendationId: String, val bestCourses: List<String>, val bestResources: List<String>, val bestProjects: List<String>, val bestResearchMaterials: List<String>, val rankingReason: String)
@Entity(tableName = "education_marketplace_course_models", indices = [Index(value = ["courseId"])])
data class EducationMarketplaceCourseModelEntity(@PrimaryKey val courseId: String, val modules: List<String>, val assignments: List<String>, val assessments: List<String>, val projects: List<String>, val integratedSystems: List<String>)
@Entity(tableName = "education_marketplace_learning_materials", indices = [Index(value = ["accessibilityReady"])])
data class EducationMarketplaceLearningMaterialEntity(@PrimaryKey val catalogId: String, val digitalCourses: List<String>, val lessons: List<String>, val videos: List<String>, val documents: List<String>, val arExperiences: List<String>, val virtualLabs: List<String>, val researchContent: List<String>, val versionControl: Boolean, val accessibilityReady: Boolean)
@Entity(tableName = "education_marketplace_resource_analytics", indices = [Index(value = ["learningEffectiveness"])])
data class EducationMarketplaceResourceAnalyticsEntity(@PrimaryKey val analyticsId: String, val resourcePopularity: List<String>, val learningEffectiveness: Int, val studentOutcomes: List<String>, val globalTrends: List<String>, val trustStatus: String)
@Entity(tableName = "education_marketplace_transactions", indices = [Index(value = ["secureTransaction"])])
data class EducationMarketplaceTransactionEntity(@PrimaryKey val transactionId: String, val learnerId: String, val resourceId: String, val copyrightProtected: Boolean, val userPrivacyProtected: Boolean, val secureTransaction: Boolean)
