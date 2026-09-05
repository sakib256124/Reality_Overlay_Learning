package com.rola.app.reasoning_ai.logical_engine

import com.rola.app.reasoning_ai.reasoning_core.ReasoningDomain
import javax.inject.Inject

class DomainReasoningManager @Inject constructor() {
    fun modelFor(domain: ReasoningDomain): String = when (domain) {
        ReasoningDomain.Mathematics -> "symbolic multi-step model"
        ReasoningDomain.Science -> "cause-effect evidence model"
        ReasoningDomain.ComputerScience -> "algorithmic reasoning model"
        ReasoningDomain.Engineering -> "constraint optimization model"
        ReasoningDomain.Business -> "tradeoff decision model"
        ReasoningDomain.Research -> "hypothesis-evidence model"
    }
}
