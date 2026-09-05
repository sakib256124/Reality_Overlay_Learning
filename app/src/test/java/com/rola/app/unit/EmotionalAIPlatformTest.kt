package com.rola.app.unit

import com.rola.app.emotional_ai.adaptation.AdaptiveEmotionLearningManager
import com.rola.app.emotional_ai.adaptation.HumanCenteredLearningManager
import com.rola.app.emotional_ai.emotion_detection.EmotionDetectionManager
import com.rola.app.emotional_ai.engagement.EngagementAnalyzer
import com.rola.app.emotional_ai.engagement.EngagementOptimizationEngine
import com.rola.app.emotional_ai.intelligence.EmotionAnalyticsEngine
import com.rola.app.emotional_ai.intelligence.EmotionalAIEngine
import com.rola.app.emotional_ai.learner_state.EmotionalLearningContext
import com.rola.app.emotional_ai.learner_state.EmotionalTone
import com.rola.app.emotional_ai.learner_state.LearnerEmotionStateManager
import com.rola.app.emotional_ai.motivation.MotivationAnalysisEngine
import com.rola.app.emotional_ai.support.EmotionAwareTeacherAgent
import com.rola.app.emotional_ai.support.EmotionalSupportAgent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EmotionalAIPlatformTest {
    private val engine = EmotionalAIEngine(
        EmotionDetectionManager(),
        LearnerEmotionStateManager(),
        MotivationAnalysisEngine(),
        EngagementAnalyzer(),
        EngagementOptimizationEngine(),
        EmotionAwareTeacherAgent(),
        EmotionalSupportAgent(),
        HumanCenteredLearningManager(),
        AdaptiveEmotionLearningManager(),
        EmotionAnalyticsEngine(),
    )

    @Test
    fun emotionalCycle_detectsConfusionAndAdaptsSupportWithPrivacy() {
        val result = engine.supportLearning(
            EmotionalLearningContext("learner-emotion", "Fractions", "I confuse the steps and this is hard.", 30, 55, listOf("hesitation", "curious"), listOf("visual lesson")),
        )

        assertEquals(EmotionalTone.Confused, result.state.tone)
        assertTrue(result.state.frustration > 50)
        assertTrue(result.motivation.strategy.contains("confidence"))
        assertTrue(result.engagement.lessonFormat.contains("visual"))
        assertTrue(result.support.teacherAdaptation.contains("slower"))
        assertTrue(result.analytics.engagementScore > 0)
        assertTrue(result.consentRequired)
    }
}
