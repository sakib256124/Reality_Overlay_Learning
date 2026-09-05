package com.rola.app.ai_os.services

import com.rola.app.ai_os.intelligence.AIOSRequest
import com.rola.app.ai_os.intelligence.EducationServiceRegistry
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EducationServiceManager @Inject constructor() {
    fun registerServices(request: AIOSRequest): EducationServiceRegistry =
        EducationServiceRegistry(
            registryId = "education-service-registry-${UUID.randomUUID()}",
            activeServices = request.requestedServices.distinct(),
            discoveryEndpoints = request.requestedServices.map { "/ai-os/services/${it.name.lowercase()}" },
            healthSummary = "Registered services support discovery, health monitoring, and dynamic activation.",
        )
}

