package com.rola.app.education_marketplace_ai.creator_network

import com.rola.app.education_marketplace_ai.marketplace_core.CreatorNetworkState
import com.rola.app.education_marketplace_ai.marketplace_core.EducationMarketplaceRequest
import javax.inject.Inject

class CreatorNetworkManager @Inject constructor() {
    fun connect(request: EducationMarketplaceRequest): CreatorNetworkState =
        CreatorNetworkState(
            creatorId = "creator-network-${request.learnerId}",
            teachers = listOf("verified teacher creator"),
            researchers = listOf("research content reviewer"),
            universities = listOf("open digital university"),
            aiCreators = listOf("Creative AI", "AI Teacher", "Knowledge Engineering"),
            organizations = listOf("education resource organization"),
            publishingEnabled = true,
        )
}
