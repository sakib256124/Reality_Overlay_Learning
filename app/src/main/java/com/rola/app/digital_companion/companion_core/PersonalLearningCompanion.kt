package com.rola.app.digital_companion.companion_core

data class PersonalLearningCompanion(
    val companionId: String,
    val userId: String,
    val personalityProfile: CompanionTone,
    val learningHistory: List<String>,
    val knowledgeUnderstanding: List<String>,
    val communicationStyle: String,
    val learningGoals: List<String>,
    val preferences: List<String>,
    val memoryControlEnabled: Boolean,
)
