package com.rola.app.global_education_network.collaboration

import com.rola.app.global_education_network.network_core.GlobalResearchNetworkState
import javax.inject.Inject

class GlobalResearchNetwork @Inject constructor() {
    fun connect(): GlobalResearchNetworkState =
        GlobalResearchNetworkState(
            researchNetworkId = "research-network-global",
            researchers = listOf("teacher researcher", "student researcher"),
            aiScientists = listOf("AI Research Scientist", "Knowledge Discovery AI"),
            universities = listOf("global open university"),
            innovationCenters = listOf("education innovation center"),
            sharedResearch = listOf("shared research notebook", "scientific collaboration challenge"),
        )
}
