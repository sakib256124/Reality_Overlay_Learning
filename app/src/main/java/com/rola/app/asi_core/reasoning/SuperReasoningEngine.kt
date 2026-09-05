package com.rola.app.asi_core.reasoning

import com.rola.app.asi_core.intelligence.ASIEducationChallenge
import com.rola.app.asi_core.intelligence.AdvancedReasoningTrace
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SuperReasoningEngine @Inject constructor(
    private val advancedReasoningEngine: AdvancedReasoningEngine,
) {
    fun solve(challenge: ASIEducationChallenge): AdvancedReasoningTrace =
        advancedReasoningEngine.reason(challenge)
}
