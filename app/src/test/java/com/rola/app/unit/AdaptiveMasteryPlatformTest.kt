package com.rola.app.unit

import com.rola.app.mastery_ai.adaptation.MasteryAdaptationManager
import com.rola.app.mastery_ai.assessment.ContinuousAssessmentEngine
import com.rola.app.mastery_ai.assessment.ProjectMasteryEngine
import com.rola.app.mastery_ai.competency.CompetencyAssessmentEngine
import com.rola.app.mastery_ai.improvement.SkillImprovementEngine
import com.rola.app.mastery_ai.mastery_engine.AdaptiveMasteryEngine
import com.rola.app.mastery_ai.mastery_engine.MasteryDecision
import com.rola.app.mastery_ai.mastery_engine.MasteryRequest
import com.rola.app.mastery_ai.mastery_engine.MasteryTeacherAgent
import com.rola.app.mastery_ai.mastery_engine.MasteryAnalyticsManager
import com.rola.app.mastery_ai.skill_analysis.LearningGapDetector
import com.rola.app.mastery_ai.skill_analysis.SkillMasteryAnalyzer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AdaptiveMasteryPlatformTest {
    private val engine = AdaptiveMasteryEngine(
        SkillMasteryAnalyzer(),
        CompetencyAssessmentEngine(),
        LearningGapDetector(),
        MasteryAdaptationManager(),
        SkillImprovementEngine(),
        MasteryTeacherAgent(),
        ProjectMasteryEngine(),
        ContinuousAssessmentEngine(),
        MasteryAnalyticsManager(),
    )

    @Test
    fun masteryCycle_detectsGapsAdaptsTeachingAndBuildsImprovementPlan() {
        val result = engine.evaluate(
            MasteryRequest(
                learnerId = "mastery-learner",
                skillName = "Programming",
                knowledgeLevel = 66,
                practicalAbility = 58,
                problemSolvingCapability = 63,
                learningConsistency = 72,
                previousPerformance = listOf(48, 55, 62, 67),
                misunderstoodTopics = listOf("state management", "debugging strategy"),
            ),
        )

        assertEquals(MasteryDecision.HumanReview, result.decision)
        assertTrue(result.profile.explainability.contains("concept understanding"))
        assertTrue(result.competency.realWorldPerformance > 0)
        assertTrue(result.gapReport.weakSkills.contains("practical application"))
        assertTrue(result.adaptation.practiceActivities.isNotEmpty())
        assertTrue(result.improvementPlan.projectRecommendations.isNotEmpty())
        assertTrue(result.teachingPlan.humanReviewSupported)
        assertTrue(result.continuousAssessment.fairnessExplanation.contains("multiple evidence"))
        assertTrue(result.analytics.biasCheck.contains("transparent"))
    }
}
