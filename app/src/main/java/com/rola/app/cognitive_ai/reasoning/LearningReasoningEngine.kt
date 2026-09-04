package com.rola.app.cognitive_ai.reasoning

import com.rola.app.domain.model.CognitiveDecision
import com.rola.app.domain.model.LearnerCognitiveProfile
import com.rola.app.domain.model.PreferredLearningMethod
import com.rola.app.domain.model.SkillLevel
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LearningReasoningEngine @Inject constructor() {
    fun reasonAboutProblem(
        profile: LearnerCognitiveProfile,
        topic: String,
    ): List<String> = buildList {
        val repeatedFailure = topic in profile.knowledgeWeaknesses
        add("Learning problem checked for $topic.")
        if (repeatedFailure) {
            add("Failure detected from repeated mistakes.")
            add("Concept gap identified in $topic.")
            add("Missing prerequisite should be retaught before advanced practice.")
            add("Simpler explanation and additional practice recommended.")
        } else {
            add("No repeated failure detected; continue adaptive challenge.")
        }
        add("Preferred method: ${profile.preferredLearningMethod.name}.")
    }

    fun adjustedExplanation(
        profile: LearnerCognitiveProfile,
        topic: String,
    ): String = when {
        topic in profile.knowledgeWeaknesses -> "Let's rebuild $topic from one simple example, then connect it to a visual model and two practice questions."
        profile.learningLevel == SkillLevel.Advanced -> "Explore $topic through mechanisms, exceptions, evidence quality, and transfer to a novel problem."
        profile.preferredLearningMethod == PreferredLearningMethod.ARModel -> "Inspect $topic as a 3D object, label evidence, then explain the relationship you observed."
        else -> "Study $topic through a short explanation, example, practice question, and reflection."
    }

    fun strategyDecision(
        profile: LearnerCognitiveProfile,
        topic: String,
    ): CognitiveDecision = CognitiveDecision(
        decisionId = "cognitive-decision-${UUID.randomUUID()}",
        userId = profile.userId,
        nextTopic = topic,
        difficultyLevel = if (topic in profile.knowledgeWeaknesses) SkillLevel.Beginner else profile.learningLevel,
        teachingStyle = profile.learningStyle,
        assessmentType = if (topic in profile.knowledgeWeaknesses) "Diagnostic reteach quiz" else "Adaptive mastery check",
        learningEnvironment = when (profile.preferredLearningMethod) {
            PreferredLearningMethod.ARModel, PreferredLearningMethod.ThreeDExplanation -> "AR/3D learning environment"
            PreferredLearningMethod.Simulation -> "Interactive simulation"
            PreferredLearningMethod.TutorConversation -> "AI tutor dialogue"
            PreferredLearningMethod.ResearchReading -> "Research assistant reading path"
            PreferredLearningMethod.PracticeQuiz -> "Quiz practice"
        },
        recommendedActivities = listOf(
            adjustedExplanation(profile, topic),
            "Practice with feedback",
            "Reflect on one previous mistake",
        ),
        explanation = reasonAboutProblem(profile, topic).joinToString(" "),
        requiresConsent = !profile.consent.cognitiveAnalysisEnabled,
    )
}
