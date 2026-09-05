package com.rola.app.digital_education_society.civilization_core

import com.rola.app.digital_education_society.knowledge_network.GlobalKnowledgeSocietyManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobalKnowledgeSociety @Inject constructor(
    private val manager: GlobalKnowledgeSocietyManager,
) {
    fun connect(challenge: GlobalEducationChallenge): GlobalKnowledgeSocietyState =
        manager.buildSociety(challenge)
}
