package com.rola.app.neural_ai.learning_state

import com.rola.app.neural_ai.cognitive_signal.ProcessedNeuralSignal
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

@Singleton
class CognitiveStateAnalyzer @Inject constructor() {
    fun analyze(signal: ProcessedNeuralSignal, topic: String): CognitiveState {
        val attentionPercent = signal.attentionScore.toPercent()
        val engagementPercent = signal.engagementScore.toPercent()
        val fatiguePercent = signal.fatigueScore.toPercent()
        val focus = when {
            signal.attentionScore >= 0.72f && signal.engagementScore >= 0.65f -> FocusLevel.High
            signal.attentionScore >= 0.45f -> FocusLevel.Moderate
            else -> FocusLevel.Low
        }
        val load = when {
            signal.cognitiveLoadScore >= 0.82f -> CognitiveLoadLevel.Overloaded
            signal.cognitiveLoadScore >= 0.64f -> CognitiveLoadLevel.High
            signal.cognitiveLoadScore >= 0.32f -> CognitiveLoadLevel.Balanced
            else -> CognitiveLoadLevel.Low
        }
        val understanding = when {
            focus == FocusLevel.High && load != CognitiveLoadLevel.Overloaded -> UnderstandingLevel.Strong
            focus == FocusLevel.Low || load == CognitiveLoadLevel.Overloaded -> UnderstandingLevel.Low
            else -> UnderstandingLevel.Developing
        }

        return CognitiveState(
            stateId = "cognitive-state-${UUID.randomUUID()}",
            userId = signal.userId,
            topic = topic,
            attentionPercent = attentionPercent,
            engagementPercent = engagementPercent,
            focusLevel = focus,
            cognitiveLoadLevel = load,
            understandingLevel = understanding,
            mentalFatiguePercent = fatiguePercent,
            explanation = buildExplanation(focus, load, understanding),
            timestamp = signal.timestamp,
        )
    }

    private fun buildExplanation(
        focus: FocusLevel,
        load: CognitiveLoadLevel,
        understanding: UnderstandingLevel,
    ): String = when {
        understanding == UnderstandingLevel.Low && load == CognitiveLoadLevel.Overloaded ->
            "The learner appears overloaded, so simplify the concept and reduce pace."
        understanding == UnderstandingLevel.Low ->
            "Attention and engagement are low, so add a concrete example and guided practice."
        load == CognitiveLoadLevel.High ->
            "The learner is working hard; keep the topic but slow the explanation."
        focus == FocusLevel.High ->
            "The learner is ready for richer AR examples and a slightly higher challenge."
        else -> "The learner is progressing with moderate support."
    }

    private fun Float.toPercent(): Int = (coerceIn(0f, 1f) * 100).roundToInt()
}
