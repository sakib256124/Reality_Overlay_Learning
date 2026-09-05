package com.rola.app.asi_core.collaboration

import com.rola.app.asi_core.intelligence.ASIEducationChallenge
import com.rola.app.asi_core.intelligence.CreativeKnowledgeOutput
import com.rola.app.asi_core.intelligence.HumanAICollaborationPlan
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HumanAICollaborationManager @Inject constructor() {
    fun createPlan(
        challenge: ASIEducationChallenge,
        creativeOutput: CreativeKnowledgeOutput,
    ): HumanAICollaborationPlan =
        HumanAICollaborationPlan(
            planId = "human-ai-collab-${UUID.randomUUID()}",
            stakeholders = challenge.stakeholders,
            aiSuggestions = creativeOutput.educationalApproaches + creativeOutput.learningActivities.take(2),
            requiredApprovals = listOf("Teacher review", "Content accuracy review", "Institution safety review"),
            feedbackLoop = "Humans approve, edit, or reject ASI suggestions before rollout.",
        )
}
