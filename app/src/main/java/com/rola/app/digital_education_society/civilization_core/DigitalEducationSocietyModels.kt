package com.rola.app.digital_education_society.civilization_core

enum class SocietyParticipantType {
    School,
    University,
    Researcher,
    Teacher,
    Student,
    AIAgent,
    EducationalRobot,
    KnowledgeSystem,
}

enum class GovernanceDecision {
    Draft,
    HumanReviewRequired,
    ApprovedPilot,
    Blocked,
}

enum class TrustLevel {
    Local,
    Verified,
    InstitutionApproved,
    GlobalReviewed,
}

data class GlobalEducationChallenge(
    val challengeId: String,
    val institutionId: String,
    val region: String,
    val topic: String,
    val knowledgeNeed: String,
    val participants: List<SocietyParticipantType>,
    val learningTrends: List<String>,
    val resourceNeeds: List<String>,
    val languages: List<String>,
)

data class GlobalKnowledgeSocietyState(
    val societyId: String,
    val connectedParticipants: List<SocietyParticipantType>,
    val sharedKnowledgeTopics: List<String>,
    val validationSteps: List<String>,
    val learningImprovementPlan: String,
)

data class AICommunityPlan(
    val communityId: String,
    val collaborationGroups: List<String>,
    val sharedResources: List<String>,
    val aiRecommendations: List<String>,
)

data class EducationEcosystemState(
    val ecosystemId: String,
    val coordinatedServices: List<String>,
    val accessibilityImprovements: List<String>,
    val resourceOptimization: String,
)

data class KnowledgeInnovationRecord(
    val innovationId: String,
    val topic: String,
    val contentIdeas: List<String>,
    val researchCollaborations: List<String>,
    val exchangeValue: String,
)

data class LearningResourceDistribution(
    val distributionId: String,
    val courses: List<String>,
    val lessons: List<String>,
    val simulations: List<String>,
    val arExperiences: List<String>,
    val distributionReason: String,
)

data class GlobalEducationIntelligenceReport(
    val reportId: String,
    val institutionId: String,
    val worldwideTrends: List<String>,
    val knowledgeGaps: List<String>,
    val futureSkillNeeds: List<String>,
    val improvementSummary: String,
)

data class DigitalGovernancePolicy(
    val policyId: String,
    val decision: GovernanceDecision,
    val trustLevel: TrustLevel,
    val accountabilityRules: List<String>,
    val dataProtectionRules: List<String>,
    val auditSummary: String,
)

data class InnovationProposal(
    val proposalId: String,
    val teachingMethods: List<String>,
    val learningTechnologies: List<String>,
    val futureClassroomConcepts: List<String>,
    val humanApprovalRequired: Boolean,
)

data class GlobalLearningPlan(
    val planId: String,
    val learnerPathways: List<String>,
    val crossCulturalSupports: List<String>,
    val multilingualSupports: List<String>,
    val personalizationSummary: String,
)

data class DigitalLearningAvatar(
    val avatarId: String,
    val learnerId: String,
    val learningHistory: List<String>,
    val skills: List<String>,
    val knowledgeLevel: String,
    val goals: List<String>,
    val achievements: List<String>,
)

data class DigitalEducationCivilizationResult(
    val resultId: String,
    val challenge: GlobalEducationChallenge,
    val knowledgeSociety: GlobalKnowledgeSocietyState,
    val communityPlan: AICommunityPlan,
    val ecosystemState: EducationEcosystemState,
    val innovationRecord: KnowledgeInnovationRecord,
    val resourceDistribution: LearningResourceDistribution,
    val analyticsReport: GlobalEducationIntelligenceReport,
    val governancePolicy: DigitalGovernancePolicy,
    val innovationProposal: InnovationProposal,
    val globalLearningPlan: GlobalLearningPlan,
    val avatar: DigitalLearningAvatar,
)
