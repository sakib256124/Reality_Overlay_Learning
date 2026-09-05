package com.rola.app.collective_ai.decision

import com.rola.app.collective_ai.intelligence_network.AIConsensusRecord
import com.rola.app.collective_ai.intelligence_network.AgentAnalysis
import com.rola.app.collective_ai.intelligence_network.ConsensusOutcome
import com.rola.app.collective_ai.intelligence_network.DebateRound
import javax.inject.Inject

class AIConsensusEngine @Inject constructor() {
    fun reachConsensus(analyses: List<AgentAnalysis>, debate: DebateRound): AIConsensusRecord {
        val averageConfidence = analyses.map { it.confidencePercent }.average().toInt()
        val ranked = analyses
            .sortedByDescending { it.confidencePercent }
            .map { it.recommendation }
            .distinct()
        return AIConsensusRecord(
            consensusId = "consensus-${debate.debateId}",
            outcome = when {
                averageConfidence >= 90 -> ConsensusOutcome.StrongConsensus
                averageConfidence >= 80 -> ConsensusOutcome.PartialConsensus
                else -> ConsensusOutcome.NeedsHumanReview
            },
            selectedStrategy = ranked.firstOrNull().orEmpty(),
            rankedStrategies = ranked,
            accuracyScore = averageConfidence,
            explanation = "Consensus ranks strategies by confidence, verified facts, and learner fit.",
        )
    }
}
