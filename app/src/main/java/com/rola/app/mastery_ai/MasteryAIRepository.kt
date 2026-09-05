package com.rola.app.mastery_ai

import com.rola.app.data.database.MasteryAIDao
import com.rola.app.data.database.entities.AssessmentResultEntity
import com.rola.app.data.database.entities.CompetencyScoreEntity
import com.rola.app.data.database.entities.ImprovementPlanEntity
import com.rola.app.data.database.entities.LearningGapEntity
import com.rola.app.data.database.entities.MasteryHistoryEntity
import com.rola.app.data.database.entities.ProjectEvaluationEntity
import com.rola.app.data.database.entities.SkillMasteryProfileEntity
import com.rola.app.data.database.entities.SkillProgressEntity
import com.rola.app.mastery_ai.mastery_engine.AdaptiveMasteryResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class MasteryAIRepository @Inject constructor(private val dao: MasteryAIDao) {
    fun observeDashboard(): Flow<MasteryDashboardState> =
        combine(
            dao.observeProfile(),
            dao.observeCompetency(),
            dao.observeGaps(),
            dao.observeProgress(),
            dao.observeHistory(),
            dao.observeAssessment(),
            dao.observeImprovement(),
            dao.observeProject(),
        ) { values ->
            val profile = values[0] as SkillMasteryProfileEntity?
            val competency = values[1] as CompetencyScoreEntity?
            val gaps = values[2] as LearningGapEntity?
            val progress = values[3] as SkillProgressEntity?
            val history = values[4] as MasteryHistoryEntity?
            val assessment = values[5] as AssessmentResultEntity?
            val improvement = values[6] as ImprovementPlanEntity?
            val project = values[7] as ProjectEvaluationEntity?
            MasteryDashboardState(
                skillName = profile?.skillName.orEmpty(),
                masteryLevel = profile?.masteryLevel.orEmpty(),
                competencyProgress = competency?.realWorldPerformance ?: 0,
                learningGaps = gaps?.missingConcepts.orEmpty() + gaps?.weakSkills.orEmpty(),
                improvementAreas = progress?.improvementAreas.orEmpty(),
                futureRecommendations = progress?.futureRecommendations.orEmpty() + improvement?.projectRecommendations.orEmpty(),
                assessmentProgress = assessment?.dailyProgress ?: 0,
                projectEvidence = project?.portfolioEvidence.orEmpty(),
                decision = history?.decision.orEmpty(),
            )
        }

    suspend fun save(result: AdaptiveMasteryResult) {
        dao.upsertProfile(SkillMasteryProfileEntity(result.profile.profileId, result.profile.skillName, result.profile.masteryLevel.name, result.profile.knowledgeLevel, result.profile.practicalAbility, result.profile.consistencyScore, result.profile.explainability))
        dao.upsertCompetency(CompetencyScoreEntity(result.competency.scoreId, result.competency.conceptMastery, result.competency.practicalApplication, result.competency.criticalThinking, result.competency.creativity, result.competency.realWorldPerformance, result.competency.level.name))
        dao.upsertGaps(LearningGapEntity(result.gapReport.gapId, result.gapReport.missingConcepts, result.gapReport.weakSkills, result.gapReport.misunderstoodTopics, result.gapReport.incorrectPatterns, result.gapReport.recommendation))
        dao.upsertProgress(SkillProgressEntity(result.analytics.analyticsId, result.analytics.masteryProgress, result.analytics.improvementAreas, result.analytics.futureRecommendations, result.analytics.biasCheck))
        dao.upsertHistory(MasteryHistoryEntity(result.resultId, result.profile.profileId, result.competency.scoreId, result.gapReport.gapId, result.decision.name, transparentEvaluation = true, humanReviewSupported = result.teachingPlan.humanReviewSupported))
        dao.upsertAssessment(AssessmentResultEntity(result.continuousAssessment.assessmentId, result.continuousAssessment.dailyProgress, result.continuousAssessment.practicalPerformance, result.continuousAssessment.knowledgeRetention, result.continuousAssessment.skillGrowth, result.continuousAssessment.fairnessExplanation))
        dao.upsertImprovement(ImprovementPlanEntity(result.improvementPlan.improvementId, result.improvementPlan.practiceTasks, result.improvementPlan.projectRecommendations, result.improvementPlan.learningChallenges, result.improvementPlan.longTermImprovement))
        dao.upsertProject(ProjectEvaluationEntity(result.projectEvaluation.projectId, result.projectEvaluation.realWorldProjects, result.projectEvaluation.practicalAssessment, result.projectEvaluation.portfolioEvidence, result.projectEvaluation.expertEvaluation))
    }
}

data class MasteryDashboardState(
    val skillName: String = "",
    val masteryLevel: String = "",
    val competencyProgress: Int = 0,
    val learningGaps: List<String> = emptyList(),
    val improvementAreas: List<String> = emptyList(),
    val futureRecommendations: List<String> = emptyList(),
    val assessmentProgress: Int = 0,
    val projectEvidence: List<String> = emptyList(),
    val decision: String = "",
)
