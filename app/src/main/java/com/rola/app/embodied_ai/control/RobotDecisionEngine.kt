package com.rola.app.embodied_ai.control

import com.rola.app.domain.model.RobotDecision
import com.rola.app.domain.model.RobotDecisionType
import com.rola.app.domain.model.RobotEmotionReport
import com.rola.app.domain.model.RobotTeachingAction
import com.rola.app.domain.model.RobotTeachingActionType
import com.rola.app.domain.model.SkillLevel
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RobotDecisionEngine @Inject constructor() {
    fun decide(
        robotId: String,
        studentId: String,
        topic: String,
        emotionReport: RobotEmotionReport,
    ): RobotDecision {
        val decisionType = when {
            emotionReport.confusionRisk >= 50 -> RobotDecisionType.ChangeTeachingStrategy
            emotionReport.engagement.name == "Low" -> RobotDecisionType.ProvideExample
            emotionReport.confidence < 60 -> RobotDecisionType.AskQuestion
            else -> RobotDecisionType.ExplainNow
        }
        val action = RobotTeachingAction(
            actionId = "robot-action-${UUID.randomUUID()}",
            robotId = robotId,
            actionType = when (decisionType) {
                RobotDecisionType.ChangeTeachingStrategy -> RobotTeachingActionType.ChangeStrategy
                RobotDecisionType.ProvideExample -> RobotTeachingActionType.ProvideFeedback
                RobotDecisionType.AskQuestion -> RobotTeachingActionType.AskCheckQuestion
                RobotDecisionType.StartExperiment -> RobotTeachingActionType.GuideExperiment
                RobotDecisionType.AlertTeacher -> RobotTeachingActionType.ProvideFeedback
                RobotDecisionType.ExplainNow -> RobotTeachingActionType.Explain
            },
            topic = topic,
            message = emotionReport.robotResponse,
            demonstration = "Use voice, gesture, and AR object reference for $topic.",
            adaptedDifficulty = if (emotionReport.confusionRisk >= 50) SkillLevel.Beginner else SkillLevel.Intermediate,
        )
        return RobotDecision(
            decisionId = "robot-decision-${UUID.randomUUID()}",
            robotId = robotId,
            studentId = studentId,
            decisionType = decisionType,
            rationale = "Decision based on engagement ${emotionReport.engagement.name}, confusion ${emotionReport.confusionRisk}, confidence ${emotionReport.confidence}.",
            teachingAction = action,
            requiresTeacherApproval = decisionType == RobotDecisionType.AlertTeacher,
        )
    }
}
