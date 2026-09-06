package com.rola.app.education_economy_ai.economy_core

enum class EconomyAssetType { Course, Textbook, ARExperience, VirtualLab, ResearchMaterial, Project, SkillCertificate }
enum class EconomyStatus { Building, Verified, Exchanging, Governed }

data class EducationEconomyRequest(
    val learnerId: String,
    val creatorGoal: String,
    val skillArea: String,
    val learningEvidence: List<String>,
    val marketSignals: List<String>,
)

data class DigitalLearningAssetPlan(val assetId: String, val assets: List<String>, val organization: List<String>, val verification: List<String>, val distribution: List<String>)
data class CreatorEconomyProfile(val creatorId: String, val teachers: List<String>, val researchers: List<String>, val developers: List<String>, val aiCreators: List<String>, val organizations: List<String>, val reputationBuilding: List<String>)
data class InnovationMarketPlan(val marketId: String, val learningResources: List<String>, val aiTools: List<String>, val educationInnovations: List<String>, val researchOutputs: List<String>, val learningSolutions: List<String>, val demandSignals: List<String>)
data class LearningValueScore(val valueId: String, val educationalEffectiveness: Int, val skillImprovement: Int, val knowledgeImpact: Int, val learnerOutcomes: List<String>, val transparentEvaluation: String)
data class SkillCertificationRecord(val certificateId: String, val verifiedCertificates: List<String>, val skillProfiles: List<String>, val competencyRecords: List<String>, val achievements: List<String>, val certificateSecure: Boolean)
data class EducationInnovationReport(val innovationId: String, val learningModels: List<String>, val teachingApproaches: List<String>, val educationTechnologies: List<String>, val aiLearningMethods: List<String>)
data class EducationReputationRecord(val reputationId: String, val creatorReputation: Int, val learnerAchievements: List<String>, val institutionRanking: String, val aiContributionScore: Int)
data class EducationEconomyReport(val analyticsId: String, val learningTrends: List<String>, val creatorActivity: List<String>, val resourcePerformance: List<String>, val globalDemand: List<String>, val economyScore: Int)
data class EconomyGovernanceState(val governanceId: String, val assetVerification: Boolean, val creatorAuthentication: Boolean, val certificateSecurity: Boolean, val dataPrivacy: Boolean, val transparentEvaluation: Boolean)
data class EducationEconomyResult(
    val resultId: String,
    val assets: DigitalLearningAssetPlan,
    val creatorProfile: CreatorEconomyProfile,
    val market: InnovationMarketPlan,
    val valueScore: LearningValueScore,
    val certification: SkillCertificationRecord,
    val innovation: EducationInnovationReport,
    val reputation: EducationReputationRecord,
    val analytics: EducationEconomyReport,
    val governance: EconomyGovernanceState,
    val status: EconomyStatus,
)
