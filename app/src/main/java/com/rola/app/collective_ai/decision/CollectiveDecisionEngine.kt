package com.rola.app.collective_ai.decision

import com.rola.app.collective_ai.intelligence_network.AIConsensusRecord
import com.rola.app.collective_ai.intelligence_network.CollectiveAIRequest
import com.rola.app.collective_ai.intelligence_network.CollectiveDecision
import com.rola.app.collective_ai.intelligence_network.ConsensusOutcome
import javax.inject.Inject

class CollectiveDecisionEngine @Inject constructor() {
    fun decide(request: CollectiveAIRequest, consensus: AIConsensusRecord): CollectiveDecision =
        CollectiveDecision(
            decisionId = "decision-${consensus.consensusId}",
            finalEducationalAction = "For ${request.topic}: ${consensus.selectedStrategy} Then deliver an explanation, AR example, and adaptive practice.",
            reasoningTrace = listOf(
                "Problem analyzed: ${request.problem}",
                "Consensus outcome: ${consensus.outcome.name}",
                "Selected strategy: ${consensus.selectedStrategy}",
                "Human feedback included: ${request.humanFeedback != null}",
            ),
            humanReviewRequired = consensus.outcome == ConsensusOutcome.NeedsHumanReview,
        )
}
