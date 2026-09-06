package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "global_network_users", indices = [Index(value = ["verified"])])
data class GlobalNetworkUserEntity(@PrimaryKey val identityId: String, val role: String, val globalProfile: String, val skillRecognition: List<String>, val learningHistory: List<String>, val verified: Boolean)
@Entity(tableName = "global_network_institutions", indices = [Index(value = ["connectionId"])])
data class GlobalNetworkInstitutionEntity(@PrimaryKey val connectionId: String, val schools: List<String>, val universities: List<String>, val researchCenters: List<String>, val trainingOrganizations: List<String>, val educationCompanies: List<String>, val jointProjects: List<String>)
@Entity(tableName = "global_network_collaboration_projects", indices = [Index(value = ["collaborationId"])])
data class GlobalNetworkCollaborationProjectEntity(@PrimaryKey val collaborationId: String, val studentCollaboration: List<String>, val teacherCollaboration: List<String>, val researchCollaboration: List<String>, val aiAgentCollaboration: List<String>, val educationalSolutions: List<String>)
@Entity(tableName = "global_network_knowledge_exchange_records", indices = [Index(value = ["exchangeId"])])
data class GlobalNetworkKnowledgeExchangeRecordEntity(@PrimaryKey val exchangeId: String, val courses: List<String>, val research: List<String>, val educationalResources: List<String>, val learningStrategies: List<String>, val innovations: List<String>)
@Entity(tableName = "global_network_courses", indices = [Index(value = ["courseId"])])
data class GlobalNetworkCourseEntity(@PrimaryKey val courseId: String, val courses: List<String>, val scholarships: List<String>, val globalProjects: List<String>, val learningCommunities: List<String>)
@Entity(tableName = "global_network_research_networks", indices = [Index(value = ["researchNetworkId"])])
data class GlobalNetworkResearchNetworkEntity(@PrimaryKey val researchNetworkId: String, val researchers: List<String>, val aiScientists: List<String>, val universities: List<String>, val innovationCenters: List<String>, val sharedResearch: List<String>)
@Entity(tableName = "global_network_international_opportunities", indices = [Index(value = ["opportunityId"])])
data class GlobalNetworkInternationalOpportunityEntity(@PrimaryKey val opportunityId: String, val courses: List<String>, val scholarships: List<String>, val researchOpportunities: List<String>, val globalProjects: List<String>, val learningCommunities: List<String>)
@Entity(tableName = "global_network_analytics", indices = [Index(value = ["globalKnowledgeGrowth"])])
data class GlobalNetworkAnalyticsEntity(@PrimaryKey val analyticsId: String, val educationTrends: List<String>, val skillDemand: List<String>, val learningPatterns: List<String>, val globalKnowledgeGrowth: Int, val recommendations: List<String>, val governanceStatus: String)
