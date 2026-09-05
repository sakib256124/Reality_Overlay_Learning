package com.rola.app.reasoning_ai.reasoning_core

import javax.inject.Inject

class PersonalReasoningProfile @Inject constructor() {
    fun build(request: ReasoningRequest): ReasoningProfile =
        ReasoningProfile("profile-${request.userId}", "step-by-step verifier", 87, listOfNotNull(request.studentAnswer?.let { "possible reasoning gap in: $it" }), listOf("practice inference", "explain each step", "connect with lifelong memory"))
}
