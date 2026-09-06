package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "education_economy_digital_learning_assets", indices = [Index(value = ["assetId"])])
data class EducationEconomyDigitalLearningAssetEntity(@PrimaryKey val assetId: String, val assets: List<String>, val organization: List<String>, val verification: List<String>, val distribution: List<String>)
@Entity(tableName = "education_economy_creator_profiles", indices = [Index(value = ["creatorId"])])
data class EducationEconomyCreatorProfileEntity(@PrimaryKey val creatorId: String, val teachers: List<String>, val researchers: List<String>, val developers: List<String>, val aiCreators: List<String>, val organizations: List<String>, val reputationBuilding: List<String>)
@Entity(tableName = "education_economy_innovations", indices = [Index(value = ["innovationId"])])
data class EducationEconomyInnovationEntity(@PrimaryKey val innovationId: String, val learningModels: List<String>, val teachingApproaches: List<String>, val educationTechnologies: List<String>, val aiLearningMethods: List<String>)
@Entity(tableName = "education_economy_skill_certificates", indices = [Index(value = ["certificateSecure"])])
data class EducationEconomySkillCertificateEntity(@PrimaryKey val certificateId: String, val verifiedCertificates: List<String>, val skillProfiles: List<String>, val competencyRecords: List<String>, val achievements: List<String>, val certificateSecure: Boolean)
@Entity(tableName = "education_economy_learning_value_scores", indices = [Index(value = ["educationalEffectiveness"])])
data class EducationEconomyLearningValueScoreEntity(@PrimaryKey val valueId: String, val educationalEffectiveness: Int, val skillImprovement: Int, val knowledgeImpact: Int, val learnerOutcomes: List<String>, val transparentEvaluation: String)
@Entity(tableName = "education_economy_transactions", indices = [Index(value = ["verifiedExchange"])])
data class EducationEconomyTransactionEntity(@PrimaryKey val transactionId: String, val learnerId: String, val assetId: String, val marketId: String, val verifiedExchange: Boolean, val dataPrivacy: Boolean)
@Entity(tableName = "education_economy_reputation_records", indices = [Index(value = ["creatorReputation"]), Index(value = ["aiContributionScore"])])
data class EducationEconomyReputationRecordEntity(@PrimaryKey val reputationId: String, val creatorReputation: Int, val learnerAchievements: List<String>, val institutionRanking: String, val aiContributionScore: Int)
@Entity(tableName = "education_economy_analytics", indices = [Index(value = ["economyScore"])])
data class EducationEconomyAnalyticsEntity(@PrimaryKey val analyticsId: String, val learningTrends: List<String>, val creatorActivity: List<String>, val resourcePerformance: List<String>, val globalDemand: List<String>, val economyScore: Int, val governanceStatus: String)
