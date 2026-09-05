package com.rola.app.ai_os.intelligence

import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DecisionOrchestrator @Inject constructor() {
    fun decide(request: AIOSRequest): AIOSDecision {
        val selectedAgent = when {
            request.userNeed.contains("quiz", ignoreCase = true) -> AIOSAgentType.Assessment
            request.userNeed.contains("research", ignoreCase = true) -> AIOSAgentType.Research
            request.userNeed.contains("robot", ignoreCase = true) -> AIOSAgentType.Robot
            else -> AIOSAgentType.Teacher
        }
        return AIOSDecision(
            decisionId = "ai-os-decision-${UUID.randomUUID()}",
            selectedAgent = selectedAgent,
            knowledgeSource = "Knowledge Graph + AI Teacher + Cognitive Memory",
            teachingMethod = "adaptive multimodal learning",
            generatedActivity = "Generate lesson, example, practice prompt, and progress check for ${request.activeTopic}.",
            explanation = "Decision selected ${selectedAgent.name} because the user need was '${request.userNeed}'.",
        )
    }
}

