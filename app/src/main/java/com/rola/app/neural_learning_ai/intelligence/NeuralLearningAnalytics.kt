package com.rola.app.neural_learning_ai.intelligence

import com.rola.app.neural_learning_ai.neural_core.NeuralLearningReport
import javax.inject.Inject

class NeuralLearningAnalytics @Inject constructor() {
    fun finalize(report: NeuralLearningReport): NeuralLearningReport =
        report.copy(recommendations = report.recommendations + "use fast knowledge retrieval for next session")
}
