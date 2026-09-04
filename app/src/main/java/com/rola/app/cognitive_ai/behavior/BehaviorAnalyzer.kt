package com.rola.app.cognitive_ai.behavior

import com.rola.app.domain.model.CognitiveActivityType
import com.rola.app.domain.model.CognitiveLearningActivity
import com.rola.app.domain.model.LearningBehaviorReport
import com.rola.app.domain.model.PreferredLearningMethod
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BehaviorAnalyzer @Inject constructor() {
    fun analyze(activities: List<CognitiveLearningActivity>): LearningBehaviorReport {
        val userId = activities.lastOrNull()?.userId.orEmpty()
        val studyMinutes = (activities.sumOf { it.durationMillis } / 60_000).toInt()
        val frequency = activities.map { it.timestamp / DAY_MILLIS }.distinct().size
        val arCount = activities.count { it.activityType == CognitiveActivityType.ARObjectExploration || it.activityType == CognitiveActivityType.Simulation }
        val quizCount = activities.count { it.activityType == CognitiveActivityType.Quiz }
        val tutorCount = activities.count { it.activityType == CognitiveActivityType.TutorConversation }
        return LearningBehaviorReport(
            reportId = "behavior-report-${UUID.randomUUID()}",
            userId = userId,
            studyDurationMinutes = studyMinutes,
            learningFrequency = frequency,
            objectScanningPattern = if (arCount >= 2) "High visual-spatial exploration" else "Limited object exploration",
            quizAttemptPattern = if (quizCount >= 2) "Frequent assessment attempts" else "Needs more retrieval practice",
            questionPattern = if (tutorCount >= 2) "Learner asks conversational follow-up questions" else "Question pattern is still emerging",
            contentInteractionPattern = dominantPattern(activities),
            recommendedMethod = when {
                arCount >= tutorCount && arCount > 0 -> PreferredLearningMethod.ARModel
                tutorCount > arCount -> PreferredLearningMethod.TutorConversation
                quizCount >= 2 -> PreferredLearningMethod.PracticeQuiz
                else -> PreferredLearningMethod.ThreeDExplanation
            },
        )
    }

    private fun dominantPattern(activities: List<CognitiveLearningActivity>): String {
        val dominant = activities.groupingBy { it.activityType }.eachCount().maxByOrNull { it.value }?.key
        return when (dominant) {
            CognitiveActivityType.ARObjectExploration -> "Learner engages through scanned objects and spatial explanations."
            CognitiveActivityType.Quiz -> "Learner engages through testing and correction."
            CognitiveActivityType.TutorConversation -> "Learner engages through dialogue."
            CognitiveActivityType.Search -> "Learner explores through self-directed search."
            CognitiveActivityType.Simulation -> "Learner engages through variable manipulation."
            CognitiveActivityType.Research -> "Learner engages through evidence and source reading."
            CognitiveActivityType.Lesson, null -> "Learner engages through guided lessons."
        }
    }

    private companion object {
        private const val DAY_MILLIS = 86_400_000L
    }
}
