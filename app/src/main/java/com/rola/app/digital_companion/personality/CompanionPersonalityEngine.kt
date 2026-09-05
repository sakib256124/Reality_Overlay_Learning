package com.rola.app.digital_companion.personality

import com.rola.app.digital_companion.companion_core.CompanionLearningContext
import com.rola.app.digital_companion.companion_core.CompanionPersonalityProfile
import com.rola.app.digital_companion.companion_core.CompanionTone
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanionPersonalityEngine @Inject constructor() {
    fun profileFor(context: CompanionLearningContext): CompanionPersonalityProfile {
        val tone = if (context.skillLevel.contains("advanced", ignoreCase = true)) {
            CompanionTone.ResearchMentor
        } else {
            CompanionTone.FriendlyTeacher
        }
        return CompanionPersonalityProfile(
            profileId = "companion-personality-${UUID.randomUUID()}",
            userId = context.userId,
            tone = tone,
            motivationStyle = if (tone == CompanionTone.ResearchMentor) "challenge with evidence" else "encourage and scaffold",
            explanationPreference = if (context.preferredModalities.contains(com.rola.app.digital_companion.companion_core.CompanionModality.ARGuidance)) {
                "visual AR explanation"
            } else {
                "step-by-step conversation"
            },
        )
    }
}

