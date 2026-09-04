package com.rola.app.ai_teacher.teaching

import com.rola.app.data.knowledgegraph.KnowledgeGraphRepository
import com.rola.app.domain.model.ClassroomAssistantResponse
import com.rola.app.domain.model.SkillLevel
import com.rola.app.domain.model.AICurriculumTeachingPlan
import com.rola.app.domain.model.TeachingPlanRequest
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClassroomAssistant @Inject constructor(
    private val knowledgeGraphRepository: KnowledgeGraphRepository,
) {
    suspend fun answerDuringClass(
        question: String,
        topic: String,
    ): ClassroomAssistantResponse {
        val context = knowledgeGraphRepository.groundedTutorContext("$topic $question")
        return ClassroomAssistantResponse(
            question = question,
            answer = if (context.isBlank()) {
                "Let's connect $topic to a visible object, then test the idea with a simple example."
            } else {
                "Using the knowledge map: ${context.lines().take(3).joinToString(" ")}"
            },
            examples = listOf("Show a real object related to $topic.", "Ask students to compare two observations."),
            demonstrations = listOf("AR scan demonstration", "Think-pair-share explanation", "One-question understanding check"),
            understandingSignal = "Ask students to explain $topic in one sentence and identify one example.",
        )
    }

    suspend fun generateTeachingPlan(request: TeachingPlanRequest): AICurriculumTeachingPlan {
        val context = knowledgeGraphRepository.groundedTutorContext("${request.subject} ${request.topic}")
        val concepts = context
            .lineSequence()
            .drop(2)
            .map { it.substringBefore(":").trim() }
            .filter { it.isNotBlank() }
            .distinct()
            .take(request.durationWeeks.coerceAtLeast(1))
            .toList()
            .ifEmpty { List(request.durationWeeks.coerceAtLeast(1)) { index -> "${request.topic} focus ${index + 1}" } }

        return AICurriculumTeachingPlan(
            planId = "teaching-plan-${UUID.randomUUID()}",
            request = request,
            weeklySequence = concepts.mapIndexed { index, concept ->
                "Week ${index + 1}: introduce $concept, run AR observation, practice, assess, and reteach gaps."
            },
            lessonSuggestions = lessonSuggestions(request.topic, request.studentLevel),
            materialSuggestions = materialSuggestions(request.topic),
            questionPrompts = questionPrompts(request.topic, request.studentLevel),
            studentAnalysisPrompts = listOf(
                "Which students missed prerequisite vocabulary?",
                "Which students need more AR evidence before abstract explanation?",
                "Which students are ready for research or design extensions?",
            ),
        )
    }

    fun lessonSuggestions(topic: String, level: SkillLevel): List<String> = when (level) {
        SkillLevel.Beginner -> listOf(
            "Start $topic with one everyday object and three observation prompts.",
            "Use vocabulary cards after students describe what they see.",
        )
        SkillLevel.Intermediate -> listOf(
            "Frame $topic as a cause-and-effect investigation.",
            "Use paired examples so students compare evidence.",
        )
        SkillLevel.Advanced -> listOf(
            "Ask students to evaluate competing explanations for $topic.",
            "Add a design challenge or research critique.",
        )
    }

    fun questionPrompts(topic: String, level: SkillLevel): List<String> = when (level) {
        SkillLevel.Beginner -> listOf("What do you notice about $topic?", "Which part shows the main idea?")
        SkillLevel.Intermediate -> listOf("What evidence explains $topic?", "How would the result change if one variable changed?")
        SkillLevel.Advanced -> listOf("What assumption does this explanation make?", "What evidence would challenge this model of $topic?")
    }

    fun materialSuggestions(topic: String): List<String> = listOf(
        "$topic vocabulary cards",
        "$topic AR scan checklist",
        "$topic misconception exit ticket",
        "$topic teacher review rubric",
    )
}
