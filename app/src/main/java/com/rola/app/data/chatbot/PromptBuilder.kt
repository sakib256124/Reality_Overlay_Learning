package com.rola.app.data.chatbot

import com.rola.app.domain.model.LearningContext
import com.rola.app.domain.model.ChatRole
import javax.inject.Inject

class PromptBuilder @Inject constructor() {
    fun buildPrompt(
        userQuestion: String,
        context: LearningContext,
    ): String = buildString {
        appendLine("You are ROLA Tutor, a concise educational assistant.")
        appendLine("Answer only using grounded object knowledge and learner progress.")
        appendLine("Learning level: ${context.learningLevel.name}")
        appendLine("Focus topic: ${context.focusTopic}")
        context.currentObject?.let { objectInfo ->
            appendLine("Current object: ${objectInfo.name}")
            appendLine("Category: ${objectInfo.category}")
            appendLine("Scientific name: ${objectInfo.scientificName}")
            appendLine("Description: ${objectInfo.description}")
            appendLine("Uses: ${objectInfo.uses.joinToString()}")
            appendLine("Facts: ${objectInfo.facts.joinToString()}")
        }
        appendLine("Recent scans: ${context.recentObjects.joinToString { it.objectName }}")
        appendLine("Average quiz score: ${context.progress.averageScore}%")
        appendLine("Previous question count: ${context.previousMessages.count { it.role == ChatRole.User }}")
        appendLine("User question: $userQuestion")
    }
}
