package com.rola.app.global_education_network

import com.rola.app.data.database.GlobalEducationNetworkDao
import com.rola.app.data.database.entities.GlobalNetworkAnalyticsEntity
import com.rola.app.data.database.entities.GlobalNetworkCollaborationProjectEntity
import com.rola.app.data.database.entities.GlobalNetworkCourseEntity
import com.rola.app.data.database.entities.GlobalNetworkInstitutionEntity
import com.rola.app.data.database.entities.GlobalNetworkInternationalOpportunityEntity
import com.rola.app.data.database.entities.GlobalNetworkKnowledgeExchangeRecordEntity
import com.rola.app.data.database.entities.GlobalNetworkResearchNetworkEntity
import com.rola.app.data.database.entities.GlobalNetworkUserEntity
import com.rola.app.global_education_network.network_core.GlobalEducationNetworkResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class GlobalEducationNetworkRepository @Inject constructor(private val dao: GlobalEducationNetworkDao) {
    fun observeDashboard(): Flow<GlobalEducationDashboardState> =
        combine(
            dao.observeUser(),
            dao.observeInstitutions(),
            dao.observeCollaboration(),
            dao.observeExchange(),
            dao.observeCourses(),
            dao.observeResearch(),
            dao.observeOpportunities(),
            dao.observeAnalytics(),
        ) { values ->
            val user = values[0] as GlobalNetworkUserEntity?
            val institutions = values[1] as GlobalNetworkInstitutionEntity?
            val collaboration = values[2] as GlobalNetworkCollaborationProjectEntity?
            val exchange = values[3] as GlobalNetworkKnowledgeExchangeRecordEntity?
            val courses = values[4] as GlobalNetworkCourseEntity?
            val research = values[5] as GlobalNetworkResearchNetworkEntity?
            val opportunities = values[6] as GlobalNetworkInternationalOpportunityEntity?
            val analytics = values[7] as GlobalNetworkAnalyticsEntity?
            GlobalEducationDashboardState(
                globalLearningNetwork = listOfNotNull(user?.globalProfile) + analytics?.educationTrends.orEmpty(),
                connectedInstitutions = institutions?.schools.orEmpty() + institutions?.universities.orEmpty() + institutions?.researchCenters.orEmpty(),
                researchActivities = research?.sharedResearch.orEmpty() + collaboration?.researchCollaboration.orEmpty(),
                learningOpportunities = opportunities?.courses.orEmpty() + opportunities?.scholarships.orEmpty() + courses?.globalProjects.orEmpty(),
                knowledgeExchange = exchange?.courses.orEmpty() + exchange?.educationalResources.orEmpty() + exchange?.innovations.orEmpty(),
                globalKnowledgeGrowth = analytics?.globalKnowledgeGrowth ?: 0,
                communication = collaboration?.aiAgentCollaboration.orEmpty(),
                governanceStatus = analytics?.governanceStatus.orEmpty(),
            )
        }

    suspend fun save(result: GlobalEducationNetworkResult) {
        val governanceStatus = "Identity ${result.governance.identityVerification}, data protection ${result.governance.dataProtection}, institution auth ${result.governance.institutionAuthentication}, privacy ${result.governance.privacyManagement}."
        dao.upsertUser(GlobalNetworkUserEntity(result.identity.identityId, result.identity.role.name, result.identity.globalProfile, result.identity.skillRecognition, result.identity.learningHistory, result.identity.verified))
        dao.upsertInstitutions(GlobalNetworkInstitutionEntity(result.institutions.connectionId, result.institutions.schools, result.institutions.universities, result.institutions.researchCenters, result.institutions.trainingOrganizations, result.institutions.educationCompanies, result.institutions.jointProjects))
        dao.upsertCollaboration(GlobalNetworkCollaborationProjectEntity(result.collaboration.collaborationId, result.collaboration.studentCollaboration, result.collaboration.teacherCollaboration, result.collaboration.researchCollaboration, result.collaboration.aiAgentCollaboration, result.collaboration.educationalSolutions))
        dao.upsertExchange(GlobalNetworkKnowledgeExchangeRecordEntity(result.exchange.exchangeId, result.exchange.courses, result.exchange.research, result.exchange.educationalResources, result.exchange.learningStrategies, result.exchange.innovations))
        dao.upsertCourses(GlobalNetworkCourseEntity("courses-${result.opportunities.opportunityId}", result.opportunities.courses, result.opportunities.scholarships, result.opportunities.globalProjects, result.opportunities.learningCommunities))
        dao.upsertResearch(GlobalNetworkResearchNetworkEntity(result.researchNetwork.researchNetworkId, result.researchNetwork.researchers, result.researchNetwork.aiScientists, result.researchNetwork.universities, result.researchNetwork.innovationCenters, result.researchNetwork.sharedResearch))
        dao.upsertOpportunities(GlobalNetworkInternationalOpportunityEntity(result.opportunities.opportunityId, result.opportunities.courses, result.opportunities.scholarships, result.opportunities.researchOpportunities, result.opportunities.globalProjects, result.opportunities.learningCommunities))
        dao.upsertAnalytics(GlobalNetworkAnalyticsEntity(result.analytics.analyticsId, result.analytics.educationTrends, result.analytics.skillDemand, result.analytics.learningPatterns, result.analytics.globalKnowledgeGrowth, result.analytics.recommendations, governanceStatus))
    }
}

data class GlobalEducationDashboardState(
    val globalLearningNetwork: List<String> = emptyList(),
    val connectedInstitutions: List<String> = emptyList(),
    val researchActivities: List<String> = emptyList(),
    val learningOpportunities: List<String> = emptyList(),
    val knowledgeExchange: List<String> = emptyList(),
    val globalKnowledgeGrowth: Int = 0,
    val communication: List<String> = emptyList(),
    val governanceStatus: String = "",
)
