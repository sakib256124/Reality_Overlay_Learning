package com.rola.app.predictive_ai.recommendation

import com.rola.app.predictive_ai.intelligence.FutureLearningRoadmap
import com.rola.app.predictive_ai.intelligence.FutureSkillModel
import com.rola.app.predictive_ai.intelligence.PotentialProfile
import javax.inject.Inject

class FutureLearningRoadmapEngine @Inject constructor() {
    fun build(potential: PotentialProfile, skillModel: FutureSkillModel): FutureLearningRoadmap =
        FutureLearningRoadmap(
            roadmapId = "future-roadmap-${potential.profileId}",
            longTermPlan = listOf("foundation mastery", "portfolio projects", "research learning", "career readiness"),
            skillRoadmap = skillModel.futureSkills,
            researchRoadmap = listOf("read papers", "build experiments", "publish learning evidence"),
            careerStrategy = potential.suggestedPath,
        )
}
