package com.rola.app.reasoning_ai.explanation

import com.rola.app.reasoning_ai.reasoning_core.ReasoningRequest
import javax.inject.Inject

class ReasoningTeacherAgent @Inject constructor() {
    fun diagnose(request: ReasoningRequest): List<String> =
        listOfNotNull(request.studentAnswer?.let { "review learner answer: $it" }, "explain mistake", "provide better approach", "build critical thinking")
}
