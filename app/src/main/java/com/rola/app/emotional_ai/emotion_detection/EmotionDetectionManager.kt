package com.rola.app.emotional_ai.emotion_detection

import com.rola.app.emotional_ai.learner_state.EmotionalLearningContext
import com.rola.app.emotional_ai.learner_state.EmotionalTone
import com.rola.app.emotional_ai.learner_state.LearnerEmotionState
import javax.inject.Inject

class EmotionDetectionManager @Inject constructor() {
    fun detect(context: EmotionalLearningContext): LearnerEmotionState {
        val confused = context.recentMessage.contains("confuse", true) || context.recentMessage.contains("hard", true)
        val confidence = context.quizConfidence.coerceIn(0, 100)
        return LearnerEmotionState(
            stateId = "emotion-${context.userId}",
            confidence = confidence,
            motivation = if (context.achievementHistory.isNotEmpty()) 82 else 68,
            interest = if (context.engagementSignals.contains("curious")) 88 else 74,
            frustration = if (confused) 62 else 24,
            confusion = if (confused) 70 else 25,
            stress = if (context.interactionSpeed < 40) 58 else 30,
            engagement = context.engagementSignals.size.times(12).plus(55).coerceAtMost(95),
            tone = if (confused) EmotionalTone.Confused else EmotionalTone.Curious,
        )
    }
}
