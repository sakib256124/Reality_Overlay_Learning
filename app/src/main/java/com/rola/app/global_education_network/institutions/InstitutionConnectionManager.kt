package com.rola.app.global_education_network.institutions

import com.rola.app.global_education_network.network_core.EducationNetworkState
import com.rola.app.global_education_network.network_core.InstitutionConnectionPlan
import javax.inject.Inject

class InstitutionConnectionManager @Inject constructor() {
    fun connect(network: EducationNetworkState): InstitutionConnectionPlan =
        InstitutionConnectionPlan(
            connectionId = "institutions-${network.networkId}",
            schools = listOf("open school network"),
            universities = listOf("global open university", "AI research university"),
            researchCenters = listOf("knowledge discovery lab", "education AI center"),
            trainingOrganizations = listOf("workforce learning alliance"),
            educationCompanies = listOf("immersive learning partner"),
            jointProjects = listOf("course exchange", "research collaboration", "global robotics project"),
        )
}
