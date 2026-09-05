package com.rola.app.creative_ai

import com.rola.app.creative_ai.creativity_engine.CreativeAIRequest
import com.rola.app.creative_ai.creativity_engine.CreativeAIResult
import com.rola.app.data.database.CreativeAIDao
import com.rola.app.data.database.entities.CreativeContentEntity
import com.rola.app.data.database.entities.CreativeEvaluationEntity
import com.rola.app.data.database.entities.CreativeGeneratedLessonEntity
import com.rola.app.data.database.entities.CreativeInnovationRecordEntity
import com.rola.app.data.database.entities.CreativeProjectEntity
import com.rola.app.data.database.entities.HumanAIProjectEntity
import com.rola.app.data.database.entities.ResearchIdeaEntity
import com.rola.app.data.database.entities.SimulationTemplateEntity
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class CreativeAIRepository @Inject constructor(private val dao: CreativeAIDao) {
    fun observeDashboard(): Flow<CreativeDashboardState> =
        combine(dao.observeContent(), dao.observeInnovation(), dao.observeResearch(), dao.observeProject(), dao.observeEvaluation()) { content, innovation, research, project, evaluation ->
            CreativeDashboardState(
                generatedContent = content?.lessons.orEmpty() + content?.activities.orEmpty(),
                newIdeas = innovation?.teachingMethods.orEmpty(),
                researchSuggestions = research?.topics.orEmpty(),
                creativeProjects = listOfNotNull(project?.solution),
                innovationHistory = innovation?.technologies.orEmpty(),
                accuracyScore = evaluation?.accuracyScore ?: 0,
                creativityScore = evaluation?.creativityScore ?: 0,
            )
        }

    suspend fun save(request: CreativeAIRequest, result: CreativeAIResult) {
        dao.upsertContent(CreativeContentEntity(result.content.contentId, result.content.lessons, result.content.examples, result.content.activities, result.content.practiceMaterials))
        dao.upsertLesson(CreativeGeneratedLessonEntity("lesson-${result.content.contentId}", request.subject, request.topic, request.level.name, request.objective))
        dao.upsertInnovation(CreativeInnovationRecordEntity(result.innovation.innovationId, result.innovation.teachingMethods, result.innovation.technologies, result.innovation.classroomStrategies, result.innovation.humanValidationRequired))
        dao.upsertResearch(ResearchIdeaEntity(result.research.researchId, result.research.topics, result.research.hypotheses, result.research.experiments, result.research.futureDirections))
        dao.upsertProject(CreativeProjectEntity(result.collaboration.projectId, result.collaboration.humanContribution, result.collaboration.aiEnhancement, result.collaboration.solution))
        dao.upsertSimulation(SimulationTemplateEntity(result.simulation.simulationId, result.simulation.virtualExperiments, result.simulation.arActivities, result.simulation.digitalTwinScenarios))
        dao.upsertEvaluation(CreativeEvaluationEntity(result.evaluation.evaluationId, result.evaluation.accuracyScore, result.evaluation.creativityScore, result.evaluation.learningEffectiveness, result.evaluation.safeForLearners, result.evaluation.explanation))
        dao.upsertHumanProject(HumanAIProjectEntity(result.collaboration.projectId, result.collaboration.humanContribution, result.collaboration.aiEnhancement, result.collaboration.solution))
    }
}

data class CreativeDashboardState(
    val generatedContent: List<String> = emptyList(),
    val newIdeas: List<String> = emptyList(),
    val researchSuggestions: List<String> = emptyList(),
    val creativeProjects: List<String> = emptyList(),
    val innovationHistory: List<String> = emptyList(),
    val accuracyScore: Int = 0,
    val creativityScore: Int = 0,
)
