package com.rola.app.global.community

import com.rola.app.domain.model.ContributionScore
import com.rola.app.domain.model.ContributorType
import com.rola.app.domain.model.RecognitionBadge
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReputationSystem @Inject constructor() {
    fun contributionScore(
        contributorId: String,
        contributorType: ContributorType,
        resourceCount: Int,
        collaborationCount: Int,
    ): ContributionScore {
        val score = resourceCount * 15 + collaborationCount * 25
        return ContributionScore(contributorId, contributorType, score, resourceCount, collaborationCount)
    }

    fun badges(score: ContributionScore): List<RecognitionBadge> = buildList {
        if (score.resourceCount >= 1) add(badge(score.contributorId, "Knowledge Contributor", "Published shared learning resources."))
        if (score.collaborationCount >= 3) add(badge(score.contributorId, "Global Collaborator", "Joined multiple international collaborations."))
        if (score.score >= 250) add(badge(score.contributorId, "Education Network Leader", "Made high-impact global contributions."))
    }

    private fun badge(contributorId: String, title: String, reason: String): RecognitionBadge =
        RecognitionBadge("badge-${UUID.randomUUID()}", contributorId, title, reason)
}
