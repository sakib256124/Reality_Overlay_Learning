package com.rola.app.asi_core.self_improvement

import com.rola.app.asi_core.intelligence.ASIEducationChallenge
import com.rola.app.asi_core.intelligence.SelfImprovementLog
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SelfImprovingEducationEngine @Inject constructor(
    private val selfImprovementEngine: SelfImprovementEngine,
) {
    fun discoverImprovements(challenge: ASIEducationChallenge): SelfImprovementLog =
        selfImprovementEngine.evaluate(challenge)
}
