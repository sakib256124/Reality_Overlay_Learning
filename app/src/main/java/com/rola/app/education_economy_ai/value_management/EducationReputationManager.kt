package com.rola.app.education_economy_ai.value_management

import com.rola.app.education_economy_ai.economy_core.CreatorEconomyProfile
import com.rola.app.education_economy_ai.economy_core.EducationReputationRecord
import com.rola.app.education_economy_ai.economy_core.SkillCertificationRecord
import javax.inject.Inject

class EducationReputationManager @Inject constructor() {
    fun build(creator: CreatorEconomyProfile, certification: SkillCertificationRecord): EducationReputationRecord =
        EducationReputationRecord(
            reputationId = "reputation-${creator.creatorId}",
            creatorReputation = 91,
            learnerAchievements = certification.achievements,
            institutionRanking = "trusted innovation partner",
            aiContributionScore = 89,
        )
}
