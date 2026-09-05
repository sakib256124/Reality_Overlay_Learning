package com.rola.app.predictive_ai.intelligence

import javax.inject.Inject

class FutureMentorAgent @Inject constructor() {
    fun guide(prediction: LearningPrediction, skills: FutureSkillModel): FutureMentorGuidance =
        FutureMentorGuidance("mentor-${prediction.predictionId}", prediction.challenges.take(3), listOf("repair gaps", "increase practice quality", "track future readiness"), skills.futureSkills)
}
