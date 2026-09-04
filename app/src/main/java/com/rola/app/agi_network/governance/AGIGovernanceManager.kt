package com.rola.app.agi_network.governance

import com.rola.app.agi_network.intelligence.AGIEducationalDecision
import com.rola.app.agi_network.intelligence.AGINetworkAccessContext
import com.rola.app.agi_network.intelligence.AGINetworkPermission
import com.rola.app.agi_network.intelligence.AGIGovernanceRecord
import com.rola.app.agi_network.intelligence.CurriculumEvolutionPlan
import com.rola.app.agi_network.intelligence.GovernanceDecision
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AGIGovernanceManager @Inject constructor() {
    fun review(
        decision: AGIEducationalDecision,
        curriculumPlan: CurriculumEvolutionPlan,
        accessContext: AGINetworkAccessContext,
    ): AGIGovernanceRecord {
        val hasSupervisor = !accessContext.humanSupervisorId.isNullOrBlank()
        val canApprove = AGINetworkPermission.ApprovePublication in accessContext.permissions
        val unsafeAutoPublish = curriculumPlan.approvalRequired && (!hasSupervisor || !canApprove)
        val governanceDecision = when {
            unsafeAutoPublish -> GovernanceDecision.NeedsHumanReview
            decision.explanation.contains("low-confidence", ignoreCase = true) -> GovernanceDecision.NeedsHumanReview
            else -> GovernanceDecision.ApprovedForDraft
        }
        return AGIGovernanceRecord(
            recordId = "agi-governance-${UUID.randomUUID()}",
            decision = governanceDecision,
            humanApprovalRequired = governanceDecision != GovernanceDecision.ApprovedForDraft,
            transparencyNotes = listOf(
                "Decision: ${decision.teachingApproach}",
                "Assessment: ${decision.assessmentStrategy}",
                "Curriculum updates are draft-only by default.",
            ),
            safetyRules = listOf(
                "No automatic unsafe publication",
                "Human override remains available",
                "Evidence must be retained for audit",
            ),
            auditSummary = if (governanceDecision == GovernanceDecision.ApprovedForDraft) {
                "Approved as draft under supervised governance."
            } else {
                "Blocked from publication until human review."
            },
        )
    }

    fun requirePermission(
        accessContext: AGINetworkAccessContext,
        permission: AGINetworkPermission,
    ) {
        require(permission in accessContext.permissions) { "Missing AGI network permission: ${permission.name}" }
    }
}
