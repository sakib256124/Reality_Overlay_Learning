package com.rola.app.emotional_ai.intelligence

import com.rola.app.emotional_ai.adaptation.AdaptiveEmotionLearningManager
import com.rola.app.emotional_ai.adaptation.HumanCenteredLearningManager
import com.rola.app.emotional_ai.emotion_detection.EmotionDetectionManager
import com.rola.app.emotional_ai.engagement.EngagementAnalyzer
import com.rola.app.emotional_ai.engagement.EngagementOptimizationEngine
import com.rola.app.emotional_ai.learner_state.EmotionalAIResult
import com.rola.app.emotional_ai.learner_state.EmotionalLearningContext
import com.rola.app.emotional_ai.learner_state.LearnerEmotionStateManager
import com.rola.app.emotional_ai.motivation.MotivationAnalysisEngine
import com.rola.app.emotional_ai.support.EmotionAwareTeacherAgent
import com.rola.app.emotional_ai.support.EmotionalSupportAgent
import javax.inject.Inject

class EmotionalAIEngine @Inject constructor(
    private val detectionManager: EmotionDetectionManager,
    private val stateManager: LearnerEmotionStateManager,
    private val motivationEngine: MotivationAnalysisEngine,
    private val engagementAnalyzer: EngagementAnalyzer,
    private val engagementOptimizationEngine: EngagementOptimizationEngine,
    private val teacherAgent: EmotionAwareTeacherAgent,
    private val supportAgent: EmotionalSupportAgent,
    private val humanCenteredLearningManager: HumanCenteredLearningManager,
    private val adaptiveEmotionLearningManager: AdaptiveEmotionLearningManager,
    private val analyticsEngine: EmotionAnalyticsEngine,
) {
    fun supportLearning(context: EmotionalLearningContext): EmotionalAIResult {
        val state = detectionManager.detect(context)
        val profile = stateManager.profile(context, state)
        val motivation = motivationEngine.analyze(context, state)
        val engagement = engagementOptimizationEngine.optimize(state)
        val teacherAdaptation = teacherAgent.adapt(context, state)
        val support = supportAgent.support(state, "${adaptiveEmotionLearningManager.apply(engagement)}; ${humanCenteredLearningManager.adapt(profile, engagement).joinToString()}; $teacherAdaptation")
        val (pattern, analytics) = analyticsEngine.analyze(state, engagementAnalyzer.score(state))
        return EmotionalAIResult("emotional-ai-${context.userId}", state, profile, motivation, engagement, support, pattern, analytics, consentRequired = true)
    }
}
