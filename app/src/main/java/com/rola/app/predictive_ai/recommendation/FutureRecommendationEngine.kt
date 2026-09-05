package com.rola.app.predictive_ai.recommendation

import com.rola.app.predictive_ai.intelligence.FutureSkillModel
import com.rola.app.predictive_ai.intelligence.PotentialProfile
import javax.inject.Inject

class FutureRecommendationEngine @Inject constructor() {
    fun recommend(potential: PotentialProfile, skills: FutureSkillModel): List<String> =
        listOf("Follow ${potential.suggestedPath}", "Prioritize ${skills.futureSkills.first()}", "Review progress monthly")
}
