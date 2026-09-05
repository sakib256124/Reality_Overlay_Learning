package com.rola.app.asi_core.governance

import com.rola.app.asi_core.intelligence.ASIApprovalStatus
import com.rola.app.asi_core.intelligence.ASIDecisionImpact
import com.rola.app.asi_core.intelligence.ASIEducationDecision
import com.rola.app.asi_core.intelligence.ASIGovernanceRecord
import com.rola.app.asi_core.intelligence.ASIRiskLevel
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ASIGovernanceManager @Inject constructor() {
    fun review(decision: ASIEducationDecision): ASIGovernanceRecord {
        val criticalImpact = decision.impact in setOf(ASIDecisionImpact.Curriculum, ASIDecisionImpact.InstitutionPolicy, ASIDecisionImpact.GlobalKnowledge)
        val status = when {
            decision.riskLevel == ASIRiskLevel.Critical -> ASIApprovalStatus.Rejected
            criticalImpact || decision.riskLevel == ASIRiskLevel.High -> ASIApprovalStatus.NeedsHumanReview
            else -> ASIApprovalStatus.DraftOnly
        }
        return ASIGovernanceRecord(
            recordId = "asi-governance-${UUID.randomUUID()}",
            approvalStatus = status,
            riskLevel = decision.riskLevel,
            transparencyNotes = listOf(decision.explanation, "Impact: ${decision.impact.name}", "Action: ${decision.action}"),
            ethicsChecks = listOf("Human control", "Privacy protection", "Educational accuracy", "Age appropriateness"),
            humanOverrideAvailable = true,
        )
    }
}
