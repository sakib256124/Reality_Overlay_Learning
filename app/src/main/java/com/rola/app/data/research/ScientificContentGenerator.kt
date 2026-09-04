package com.rola.app.data.research

import com.rola.app.domain.model.Flashcard
import com.rola.app.domain.model.KnowledgeUpdate
import com.rola.app.domain.model.LearningMaterial
import com.rola.app.domain.model.LearningMaterialType
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScientificContentGenerator @Inject constructor() {
    fun generateMaterials(update: KnowledgeUpdate): List<LearningMaterial> {
        val article = material(update, LearningMaterialType.EducationalArticle)
        val studyGuide = material(update, LearningMaterialType.StudyGuide)
        val quizBank = material(update, LearningMaterialType.QuizBank)
        return listOf(article, studyGuide, quizBank)
    }

    private fun material(
        update: KnowledgeUpdate,
        type: LearningMaterialType,
    ): LearningMaterial {
        val topic = update.topic
        return LearningMaterial(
            materialId = "material-${type.name.lowercase()}-${UUID.randomUUID()}",
            topic = topic,
            materialType = type,
            title = when (type) {
                LearningMaterialType.ObjectDescription -> "$topic Object Notes"
                LearningMaterialType.EducationalArticle -> "$topic Explained"
                LearningMaterialType.QuizBank -> "$topic Practice Questions"
                LearningMaterialType.PracticeExercise -> "$topic Practice"
                LearningMaterialType.StudyGuide -> "$topic Study Guide"
            },
            beginnerExplanation = beginnerExplanation(update),
            advancedExplanation = advancedExplanation(update),
            summary = update.summary,
            quizQuestions = quizQuestions(update),
            flashcards = flashcards(update),
            sourceUpdateId = update.updateId,
            version = update.version,
        )
    }

    private fun beginnerExplanation(update: KnowledgeUpdate): String {
        val definition = update.definitions.firstOrNull() ?: "${update.topic} is something we can study by observing examples."
        return "$definition ${update.examples.firstOrNull() ?: "Look for it in everyday objects."}"
    }

    private fun advancedExplanation(update: KnowledgeUpdate): String {
        val properties = update.properties.joinToString(separator = " ")
        val applications = update.applications.joinToString(separator = " ")
        return listOf(
            "${update.topic} can be studied through evidence, properties, relationships, and applications.",
            properties,
            applications,
        ).filter { it.isNotBlank() }.joinToString(separator = " ")
    }

    private fun quizQuestions(update: KnowledgeUpdate): List<String> = listOf(
        "What is ${update.topic}?",
        "Which property helps explain ${update.topic}?",
        "How is ${update.topic} used or observed in real life?",
        "Which related concept connects most strongly to ${update.topic}?",
    )

    private fun flashcards(update: KnowledgeUpdate): List<Flashcard> = listOf(
        Flashcard("Define ${update.topic}", update.definitions.firstOrNull() ?: update.summary),
        Flashcard("Application", update.applications.firstOrNull() ?: "Connect it to an observed object."),
        Flashcard("Example", update.examples.firstOrNull() ?: "Find a real-world example and compare it."),
    )
}
