package com.rola.app.core.ai

import com.rola.app.data.adaptive.UserProfileRepository
import com.rola.app.data.knowledgegraph.KnowledgeGraphRepository
import com.rola.app.data.research.ResearchTutorBridge
import com.rola.app.domain.model.DetectedObject
import com.rola.app.domain.model.LearningPreferences
import com.rola.app.domain.model.LearningProfile
import com.rola.app.domain.model.LearningSpeed
import com.rola.app.domain.model.SceneContext
import com.rola.app.domain.model.SkillLevel
import com.rola.app.domain.model.UnifiedLearningContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first

@Singleton
class ContextManager @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    private val knowledgeGraphRepository: KnowledgeGraphRepository,
    private val researchTutorBridge: ResearchTutorBridge,
) {
    suspend fun buildContext(
        objective: String,
        detectedObjects: List<DetectedObject> = emptyList(),
        sceneContext: SceneContext? = null,
        locationContext: String? = null,
    ): UnifiedLearningContext {
        val profile = userProfileRepository.observeProfile().first() ?: userProfileRepository.refreshLearningProfile()
        val query = detectedObjects.firstOrNull()?.label ?: objective
        return fromProfile(
            profile = profile,
            objective = objective,
            detectedObjects = detectedObjects,
            sceneContext = sceneContext,
            locationContext = locationContext,
            knowledgeGraphContext = knowledgeGraphRepository.groundedTutorContext(query),
            researchContext = researchTutorBridge.buildResearchEnhancedContext(query),
        )
    }

    fun fallbackContext(userId: String = "local_user", objective: String): UnifiedLearningContext =
        UnifiedLearningContext(
            userId = userId,
            learningLevel = SkillLevel.Beginner,
            previousKnowledge = emptyList(),
            preferences = LearningPreferences(learningSpeed = LearningSpeed.Balanced),
            language = "en",
            currentObjective = objective,
        )

    private fun fromProfile(
        profile: LearningProfile,
        objective: String,
        detectedObjects: List<DetectedObject>,
        sceneContext: SceneContext?,
        locationContext: String?,
        knowledgeGraphContext: String,
        researchContext: String,
    ): UnifiedLearningContext = UnifiedLearningContext(
        userId = profile.userId,
        learningLevel = profile.learningLevel,
        previousKnowledge = profile.favoriteCategories + profile.frequentlySearchedTopics,
        preferences = LearningPreferences(
            personalizationEnabled = profile.personalizationEnabled,
            learningSpeed = profile.learningSpeed,
            favoriteCategories = profile.favoriteCategories,
            interestAreas = profile.frequentlySearchedTopics,
        ),
        language = profile.preferredLanguage,
        detectedObjects = detectedObjects,
        sceneContext = sceneContext,
        locationContext = locationContext,
        currentObjective = objective,
        knowledgeGraphContext = knowledgeGraphContext,
        researchContext = researchContext,
    )
}
