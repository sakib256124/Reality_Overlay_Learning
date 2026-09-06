package com.rola.app.global_education_network.network_core

enum class GlobalParticipantRole { Learner, Teacher, Researcher, University, Organization, AIAgent }
enum class GlobalNetworkStatus { Connecting, Active, Collaborative, Governed }

data class GlobalEducationNetworkRequest(
    val userId: String,
    val region: String,
    val goals: List<String>,
    val skills: List<String>,
    val interests: List<String>,
    val learningHistory: List<String>,
    val preferredLanguage: String,
)

data class GlobalEducationIdentity(val identityId: String, val role: GlobalParticipantRole, val globalProfile: String, val skillRecognition: List<String>, val learningHistory: List<String>, val verified: Boolean)
data class EducationNetworkState(val networkId: String, val communities: List<String>, val aiSystems: List<String>, val globalSync: Boolean, val cacheStrategy: String)
data class InstitutionConnectionPlan(val connectionId: String, val schools: List<String>, val universities: List<String>, val researchCenters: List<String>, val trainingOrganizations: List<String>, val educationCompanies: List<String>, val jointProjects: List<String>)
data class GlobalCollaborationPlan(val collaborationId: String, val studentCollaboration: List<String>, val teacherCollaboration: List<String>, val researchCollaboration: List<String>, val aiAgentCollaboration: List<String>, val educationalSolutions: List<String>)
data class KnowledgeExchangePlan(val exchangeId: String, val courses: List<String>, val research: List<String>, val educationalResources: List<String>, val learningStrategies: List<String>, val innovations: List<String>)
data class GlobalOpportunityPlan(val opportunityId: String, val courses: List<String>, val scholarships: List<String>, val researchOpportunities: List<String>, val globalProjects: List<String>, val learningCommunities: List<String>)
data class GlobalCommunicationPlan(val communicationId: String, val realTimeTranslation: Boolean, val crossLanguageLearning: List<String>, val internationalDiscussion: List<String>, val culturalAdaptation: List<String>)
data class GlobalResearchNetworkState(val researchNetworkId: String, val researchers: List<String>, val aiScientists: List<String>, val universities: List<String>, val innovationCenters: List<String>, val sharedResearch: List<String>)
data class GlobalEducationIntelligenceReport(val analyticsId: String, val educationTrends: List<String>, val skillDemand: List<String>, val learningPatterns: List<String>, val globalKnowledgeGrowth: Int, val recommendations: List<String>)
data class NetworkGovernanceState(val governanceId: String, val identityVerification: Boolean, val dataProtection: Boolean, val institutionAuthentication: Boolean, val privacyManagement: Boolean, val globalPolicies: List<String>)
data class GlobalEducationNetworkResult(
    val resultId: String,
    val identity: GlobalEducationIdentity,
    val network: EducationNetworkState,
    val institutions: InstitutionConnectionPlan,
    val collaboration: GlobalCollaborationPlan,
    val exchange: KnowledgeExchangePlan,
    val opportunities: GlobalOpportunityPlan,
    val communication: GlobalCommunicationPlan,
    val researchNetwork: GlobalResearchNetworkState,
    val analytics: GlobalEducationIntelligenceReport,
    val governance: NetworkGovernanceState,
    val status: GlobalNetworkStatus,
)
