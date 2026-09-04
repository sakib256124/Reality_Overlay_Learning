package com.rola.app.data.chatbot

import com.rola.app.domain.model.LearningContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LLMService @Inject constructor(
    private val promptBuilder: PromptBuilder,
) {
    suspend fun generateResponse(
        question: String,
        context: LearningContext,
    ): String {
        val prompt = promptBuilder.buildPrompt(question, context)
        return localGroundedResponse(question = question, context = context, prompt = prompt)
    }

    suspend fun summarizeConcept(context: LearningContext): String =
        context.currentObject?.let { info ->
            "${info.name} is a ${info.category}. ${info.description}"
        } ?: "I can summarize an object after you scan or select one."

    suspend fun explainSimply(
        question: String,
        context: LearningContext,
    ): String =
        generateResponse("Explain simply: $question", context)

    suspend fun generateExamples(context: LearningContext): String =
        context.currentObject?.uses
            ?.takeIf { it.isNotEmpty() }
            ?.joinToString(prefix = "Examples include ", separator = ", ", postfix = ".")
            ?: "I need object information before I can give grounded examples."

    private fun localGroundedResponse(
        question: String,
        context: LearningContext,
        prompt: String,
    ): String {
        val objectInfo = context.currentObject
            ?: return personalizedFallback(context)

        val lowerQuestion = question.lowercase()
        val base = when {
            "beginner" in lowerQuestion || "simple" in lowerQuestion -> {
                "Think of ${objectInfo.name} as a ${objectInfo.category.lowercase()} you can study by looking at what it is made of, what it does, and where people use it. ${objectInfo.description}"
            }
            "made" in lowerQuestion || "material" in lowerQuestion || "scientific" in lowerQuestion -> {
                "${objectInfo.name} is connected to this scientific information: ${objectInfo.scientificName}. ${objectInfo.description}"
            }
            "use" in lowerQuestion || "application" in lowerQuestion || "example" in lowerQuestion -> {
                val uses = objectInfo.uses.joinToString(separator = ", ").ifBlank { "observation and learning" }
                "${objectInfo.name} is commonly used for $uses. A real-world way to remember it is to connect the object with the job it helps people do."
            }
            "quiz" in lowerQuestion || "test" in lowerQuestion || "prepare" in lowerQuestion -> {
                "For quiz prep, remember three things about ${objectInfo.name}: its category is ${objectInfo.category}, its scientific information is ${objectInfo.scientificName}, and one key use is ${objectInfo.uses.firstOrNull() ?: "learning by observation"}."
            }
            "fact" in lowerQuestion -> {
                objectInfo.facts.firstOrNull()
                    ?.let { "An interesting fact about ${objectInfo.name}: $it" }
                    ?: "I do not have a verified fact for ${objectInfo.name} yet, so I should not invent one."
            }
            else -> {
                "${objectInfo.name} is a ${objectInfo.category}. ${objectInfo.description} Its scientific information is ${objectInfo.scientificName}."
            }
        }

        return appendPersonalGuidance(base, context, prompt)
    }

    private fun personalizedFallback(context: LearningContext): String {
        val recent = context.recentObjects.firstOrNull()
        return if (recent != null) {
            "I do not have a selected object right now. Based on your recent learning, you could ask about ${recent.objectName}, ${recent.category}, or quiz preparation."
        } else {
            "I can help once you scan or search for an object. Try asking what an object is made of, how it is used, or how to prepare for a quiz."
        }
    }

    private fun appendPersonalGuidance(
        response: String,
        context: LearningContext,
        prompt: String,
    ): String {
        val guidance = when {
            context.progress.averageScore in 1..59 ->
                "Since your recent quiz average is ${context.progress.averageScore}%, review the category and material first."
            context.progress.averageScore >= 85 ->
                "Your quiz performance is strong, so try explaining the object in your own words next."
            else ->
                "A good next step is to compare this object with another object in the same category."
        }

        return "$response $guidance"
            .take(MAX_RESPONSE_LENGTH)
            .ifBlank { prompt.take(MAX_RESPONSE_LENGTH) }
    }

    private companion object {
        const val MAX_RESPONSE_LENGTH = 900
    }
}
