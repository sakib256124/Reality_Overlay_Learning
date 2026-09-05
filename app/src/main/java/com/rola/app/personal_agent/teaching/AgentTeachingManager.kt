package com.rola.app.personal_agent.teaching

import com.rola.app.personal_agent.agent_core.AgentRequest
import com.rola.app.personal_agent.agent_core.AgentTeachingResponse
import javax.inject.Inject

class AgentTeachingManager @Inject constructor() {
    fun teach(request: AgentRequest): AgentTeachingResponse =
        AgentTeachingResponse(
            teachingId = "teach-${request.userId}",
            explanation = "Explain ${request.currentGoal} with ${request.skillLevel} examples, ${request.emotionState} support, and ${request.learningSpeed} pacing.",
            examples = listOf("worked example", "AR-guided example", "career-linked example"),
            exercises = request.previousMistakes.map { "practice correction for $it" }.ifEmpty { listOf("diagnostic exercise", "project exercise") },
            evaluationPrompt = "Ask the learner to explain the idea, solve one task, and rate confidence.",
        )
}
