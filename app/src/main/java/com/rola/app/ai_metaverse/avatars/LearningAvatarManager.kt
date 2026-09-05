package com.rola.app.ai_metaverse.avatars

import com.rola.app.ai_metaverse.intelligence.AvatarRole
import com.rola.app.ai_metaverse.intelligence.LearningAvatar
import com.rola.app.ai_metaverse.intelligence.MetaverseLearningRequest
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LearningAvatarManager @Inject constructor() {
    fun createAvatar(request: MetaverseLearningRequest): LearningAvatar =
        LearningAvatar(
            avatarId = "learning-avatar-${UUID.randomUUID()}",
            learnerId = request.learnerId,
            displayName = "ROLA Avatar",
            role = AvatarRole.Student,
            learningHistory = listOf("${request.topic} exploration", "AR object learning", "AI tutor conversation"),
            skills = listOf(request.subject, "Collaboration", "Virtual experimentation"),
            achievements = listOf("Entered persistent campus", "Joined ${request.collaborationMode} session"),
            knowledgeLevel = request.studentLevel,
            personalityProfile = "Curious, visual, mentor-guided",
            learningGoals = listOf(request.learningObjective, "Explain ${request.topic} with evidence"),
        )
}

