package com.rola.app.ai_civilization.intelligence

import javax.inject.Inject

class UniversalAIEducator @Inject constructor() {
    fun teach(context: CivilizationContext): String = when (context.level) {
        CivilizationLearningLevel.Beginner -> "Teach ${context.topic} with simple examples."
        CivilizationLearningLevel.Intermediate -> "Teach ${context.topic} with adaptive practice and visual models."
        CivilizationLearningLevel.Advanced -> "Teach ${context.topic} with expert reasoning and cross-domain links."
        CivilizationLearningLevel.Expert -> "Guide ${context.topic} through research-grade problem solving."
        CivilizationLearningLevel.Research -> "Support ${context.topic} as an open research and discovery path."
    }
}
