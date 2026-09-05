package com.rola.app.digital_education_society.avatar

import com.rola.app.digital_education_society.civilization_core.DigitalLearningAvatar
import com.rola.app.digital_education_society.civilization_core.GlobalEducationChallenge
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DigitalLearningAvatarManager @Inject constructor() {
    fun buildAvatar(challenge: GlobalEducationChallenge): DigitalLearningAvatar =
        DigitalLearningAvatar(
            avatarId = "digital-avatar-${UUID.randomUUID()}",
            learnerId = "global-learner-${challenge.institutionId}",
            learningHistory = challenge.learningTrends,
            skills = listOf("Knowledge sharing", "AI collaboration", "Global citizenship", challenge.topic),
            knowledgeLevel = "Developing global readiness",
            goals = listOf("Close knowledge need: ${challenge.knowledgeNeed}", "Use approved resources responsibly"),
            achievements = listOf("Joined ${challenge.region} knowledge society", "Created supervised learning plan"),
        )
}

