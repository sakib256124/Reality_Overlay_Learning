package com.rola.app.domain.model

data class LearningContext(
    val userId: String,
    val currentObject: ObjectInformation?,
    val recentObjects: List<ScanHistory>,
    val progress: LearningProgress,
    val previousMessages: List<ChatMessage>,
    val learningLevel: LearningLevel = LearningLevel.Beginner,
) {
    val focusTopic: String
        get() = currentObject?.category
            ?: recentObjects.firstOrNull()?.category
            ?: "general object science"
}

enum class LearningLevel {
    Beginner,
    Intermediate,
    Advanced,
}
