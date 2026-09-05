package com.rola.app.mastery_ai.mastery_engine

import com.rola.app.mastery_ai.adaptation.MasteryAdaptationManager
import com.rola.app.mastery_ai.assessment.ContinuousAssessmentEngine
import com.rola.app.mastery_ai.assessment.ProjectMasteryEngine
import com.rola.app.mastery_ai.competency.CompetencyAssessmentEngine
import com.rola.app.mastery_ai.improvement.SkillImprovementEngine
import com.rola.app.mastery_ai.skill_analysis.LearningGapDetector
import com.rola.app.mastery_ai.skill_analysis.SkillMasteryAnalyzer
import javax.inject.Inject

class AdaptiveMasteryEngine @Inject constructor(
    private val skillMasteryAnalyzer: SkillMasteryAnalyzer,
    private val competencyAssessmentEngine: CompetencyAssessmentEngine,
    private val learningGapDetector: LearningGapDetector,
    private val masteryAdaptationManager: MasteryAdaptationManager,
    private val skillImprovementEngine: SkillImprovementEngine,
    private val masteryTeacherAgent: MasteryTeacherAgent,
    private val projectMasteryEngine: ProjectMasteryEngine,
    private val continuousAssessmentEngine: ContinuousAssessmentEngine,
    private val masteryAnalyticsManager: MasteryAnalyticsManager,
) {
    fun evaluate(request: MasteryRequest): AdaptiveMasteryResult {
        val profile = skillMasteryAnalyzer.analyze(request)
        val competency = competencyAssessmentEngine.assess(request)
        val gaps = learningGapDetector.detect(request)
        val assessment = continuousAssessmentEngine.assess(request)
        val analytics = masteryAnalyticsManager.summarize(profile, competency, gaps, assessment)
        return AdaptiveMasteryResult(
            resultId = "mastery-${request.learnerId}",
            profile = profile,
            competency = competency,
            gapReport = gaps,
            adaptation = masteryAdaptationManager.adapt(profile, gaps),
            improvementPlan = skillImprovementEngine.improve(request, gaps),
            teachingPlan = masteryTeacherAgent.teach(profile, gaps),
            projectEvaluation = projectMasteryEngine.evaluate(request),
            continuousAssessment = assessment,
            analytics = analytics,
            decision = when {
                analytics.masteryProgress >= 88 && gaps.weakSkills.isEmpty() -> MasteryDecision.StartProject
                analytics.masteryProgress >= 75 -> MasteryDecision.AdvanceDifficulty
                gaps.weakSkills.size >= 3 -> MasteryDecision.HumanReview
                else -> MasteryDecision.ContinuePractice
            },
        )
    }
}
