package com.rola.app.unit

import com.rola.app.digital_education_society.ai_society.AICommunityCoordinator
import com.rola.app.digital_education_society.analytics.GlobalEducationAnalyticsEngine
import com.rola.app.digital_education_society.avatar.DigitalLearningAvatarManager
import com.rola.app.digital_education_society.civilization_core.DigitalEducationCivilizationEngine
import com.rola.app.digital_education_society.civilization_core.GlobalEducationChallenge
import com.rola.app.digital_education_society.civilization_core.GovernanceDecision
import com.rola.app.digital_education_society.civilization_core.SocietyParticipantType
import com.rola.app.digital_education_society.collaboration.GlobalLearningCoordinator
import com.rola.app.digital_education_society.ecosystem.EducationEcosystemManager
import com.rola.app.digital_education_society.governance.DigitalGovernanceManager
import com.rola.app.digital_education_society.innovation.InnovationEngine
import com.rola.app.digital_education_society.innovation.KnowledgeInnovationEngine
import com.rola.app.digital_education_society.knowledge_network.GlobalKnowledgeSocietyManager
import com.rola.app.digital_education_society.resources.LearningResourceManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DigitalEducationSocietyPlatformTest {
    private val engine = DigitalEducationCivilizationEngine(
        knowledgeSocietyManager = GlobalKnowledgeSocietyManager(),
        communityCoordinator = AICommunityCoordinator(),
        ecosystemManager = EducationEcosystemManager(),
        knowledgeInnovationEngine = KnowledgeInnovationEngine(),
        resourceManager = LearningResourceManager(),
        analyticsEngine = GlobalEducationAnalyticsEngine(),
        governanceManager = DigitalGovernanceManager(),
        innovationEngine = InnovationEngine(),
        globalLearningCoordinator = GlobalLearningCoordinator(),
        avatarManager = DigitalLearningAvatarManager(),
    )

    @Test
    fun civilizationCycle_connectsKnowledgeSocietyAndCommunities() {
        val result = engine.buildCivilization(sampleChallenge())

        assertTrue(result.knowledgeSociety.connectedParticipants.contains(SocietyParticipantType.KnowledgeSystem))
        assertTrue(result.communityPlan.collaborationGroups.any { it.contains("teacher", ignoreCase = true) })
        assertTrue(result.innovationRecord.contentIdeas.any { it.contains("validated", ignoreCase = true) })
    }

    @Test
    fun civilizationCycle_distributesResourcesAndBuildsAvatar() {
        val result = engine.buildCivilization(sampleChallenge())

        assertTrue(result.resourceDistribution.arExperiences.any { it.contains("AR", ignoreCase = true) })
        assertTrue(result.globalLearningPlan.multilingualSupports.any { it.contains("Bangla") })
        assertTrue(result.avatar.skills.contains("Global citizenship"))
    }

    @Test
    fun civilizationCycle_keepsGlobalChangesHumanGoverned() {
        val result = engine.buildCivilization(sampleChallenge())

        assertEquals(GovernanceDecision.HumanReviewRequired, result.governancePolicy.decision)
        assertTrue(result.governancePolicy.dataProtectionRules.any { it.contains("Anonymize") })
        assertTrue(result.innovationProposal.humanApprovalRequired)
    }

    private fun sampleChallenge(): GlobalEducationChallenge =
        GlobalEducationChallenge(
            challengeId = "digital-society-test",
            institutionId = "institution-global-1",
            region = "Global South learning network",
            topic = "Climate Science",
            knowledgeNeed = "Localized experiments and multilingual explanations",
            participants = listOf(
                SocietyParticipantType.School,
                SocietyParticipantType.University,
                SocietyParticipantType.Teacher,
                SocietyParticipantType.Student,
                SocietyParticipantType.Researcher,
                SocietyParticipantType.EducationalRobot,
            ),
            learningTrends = listOf("AR science labs", "AI-assisted tutoring", "community research projects"),
            resourceNeeds = listOf("offline lessons", "simulation labs", "teacher-reviewed AR activities"),
            languages = listOf("English", "Bangla"),
        )
}
