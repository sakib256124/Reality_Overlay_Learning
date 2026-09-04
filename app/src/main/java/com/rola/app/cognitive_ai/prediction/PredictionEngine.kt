package com.rola.app.cognitive_ai.prediction

import com.rola.app.domain.model.LearnerCognitiveProfile
import com.rola.app.domain.model.LearningPrediction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PredictionEngine @Inject constructor(
    private val learningPredictionEngine: LearningPredictionEngine,
) {
    fun futureRoadmap(profile: LearnerCognitiveProfile): LearningPrediction =
        learningPredictionEngine.predict(profile)
}
