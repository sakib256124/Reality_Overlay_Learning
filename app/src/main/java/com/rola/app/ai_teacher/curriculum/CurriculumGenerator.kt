package com.rola.app.ai_teacher.curriculum

import com.rola.app.ai_teacher.assessment.AssessmentGenerator
import com.rola.app.ai_teacher.lesson.LessonGenerator
import com.rola.app.ai_teacher.teaching.CurriculumQualityValidator
import com.rola.app.data.knowledgegraph.KnowledgeGraphRepository
import com.rola.app.domain.model.CurriculumModule
import com.rola.app.domain.model.CurriculumRequest
import com.rola.app.domain.model.GeneratedCurriculum
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurriculumGenerator @Inject constructor(
    private val knowledgeGraphRepository: KnowledgeGraphRepository,
    private val lessonGenerator: LessonGenerator,
    private val assessmentGenerator: AssessmentGenerator,
    private val qualityValidator: CurriculumQualityValidator,
) {
    suspend fun generateCurriculum(
        teacherId: String,
        institutionId: String,
        request: CurriculumRequest,
    ): GeneratedCurriculum {
        val search = knowledgeGraphRepository.semanticSearch("${request.subject} ${request.topic}")
        val conceptNames = search.graph.nodes.map { it.name }.distinct().take(6).ifEmpty {
            listOf(request.topic, request.learningObjective)
        }
        val moduleCount = request.durationWeeks.coerceIn(1, 8)
        val modules = (1..moduleCount).map { index ->
            val concept = conceptNames.getOrElse(index - 1) { "${request.topic} application $index" }
            val moduleObjective = when (index) {
                1 -> request.learningObjective
                moduleCount -> "Apply $concept to an authentic problem about ${request.topic}."
                else -> "Connect $concept to ${request.topic} using evidence, examples, and AR observation."
            }
            val lessons = listOf(
                lessonGenerator.generateLesson(
                    topic = concept,
                    objective = moduleObjective,
                    level = request.studentLevel,
                    relatedConcepts = conceptNames,
                ),
                lessonGenerator.generateLesson(
                    topic = "$concept application",
                    objective = "Use $concept to solve a grade-level problem connected to ${request.learningObjective}",
                    level = request.studentLevel,
                    relatedConcepts = conceptNames.drop(index - 1) + conceptNames.take(index - 1),
                ),
            )
            CurriculumModule(
                moduleId = "module-${UUID.randomUUID()}",
                title = "Module $index: $concept",
                objective = moduleObjective,
                lessons = lessons,
                estimatedHours = lessons.size * 2,
            )
        }
        val assessments = modules.map { module ->
            assessmentGenerator.generateAssessment(
                topic = module.title.substringAfter(": ").ifBlank { request.topic },
                level = request.studentLevel,
            )
        }
        val draft = GeneratedCurriculum(
            curriculumId = "curriculum-${UUID.randomUUID()}",
            teacherId = teacherId,
            institutionId = institutionId,
            request = request,
            title = "${request.gradeLevel} ${request.subject}: ${request.topic}",
            overview = buildString {
                append("A ${request.durationWeeks}-week AR-supported course for ${request.learningObjective}. ")
                append("It contains ${modules.size} modules, ${modules.sumOf { it.lessons.size }} lessons, ")
                append("${modules.flatMap { it.lessons }.sumOf { it.arActivities.size }} AR activities, experiments, and staged assessments.")
            },
            modules = modules,
            assessments = assessments,
            quality = qualityValidator.placeholderReport(),
        )
        return draft.copy(quality = qualityValidator.validate(draft, search.explanation))
    }
}
