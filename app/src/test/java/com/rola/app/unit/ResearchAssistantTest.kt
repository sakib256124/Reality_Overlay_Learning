package com.rola.app.unit

import com.rola.app.data.research.CollectedKnowledge
import com.rola.app.data.research.ContentAnalyzer
import com.rola.app.data.research.KnowledgeVerificationEngine
import com.rola.app.data.research.ScientificContentGenerator
import com.rola.app.data.research.SourceManager
import com.rola.app.domain.model.KnowledgeUpdate
import com.rola.app.domain.model.KnowledgeVerification
import com.rola.app.domain.model.ModerationStatus
import com.rola.app.domain.model.ResearchPriority
import com.rola.app.domain.model.ResearchTask
import com.rola.app.domain.model.ScientificSource
import com.rola.app.domain.model.ScientificSourceType
import com.rola.app.domain.model.SkillLevel
import com.rola.app.domain.model.SourceReliability
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ResearchAssistantTest {
    private val sourceManager = SourceManager()
    private val analyzer = ContentAnalyzer()
    private val verifier = KnowledgeVerificationEngine(sourceManager)
    private val generator = ScientificContentGenerator()

    @Test
    fun analyzerExtractsPlantRelationshipsAndDifficulty() {
        val analysis = analyzer.analyze(
            collectedKnowledge(
                topic = "Plant cellular structure",
                text = "Plant biology includes cell organization, energy flow, and photosynthesis.",
            ),
        )

        assertEquals(SkillLevel.Beginner, analysis.difficultyLevel)
        assertTrue(analysis.properties.any { it.contains("cellular", ignoreCase = true) })
        assertTrue(analysis.relationships.any { it.targetName == "Plant" })
    }

    @Test
    fun verificationApprovesReliableNonDuplicateSources() {
        val verification = verifier.verify(
            collectedKnowledge("Photosynthesis mechanism"),
            existingUpdates = emptyList(),
        )

        assertTrue(verification.reliabilityScore >= 0.65f)
        assertTrue(verification.isApprovalReady)
    }

    @Test
    fun contentGeneratorCreatesLearningMaterialSet() {
        val update = KnowledgeUpdate(
            updateId = "update-1",
            taskId = "task-1",
            topic = "Photosynthesis",
            summary = "Photosynthesis turns light energy into chemical energy.",
            definitions = listOf("Photosynthesis is how plants make food using light."),
            properties = listOf("It uses light, water, and carbon dioxide."),
            applications = listOf("It explains plant growth and ecosystems."),
            examples = listOf("Leaves use chlorophyll to capture light."),
            relationSuggestions = emptyList(),
            difficultyLevel = SkillLevel.Beginner,
            sourceIds = listOf("source-1"),
            verification = KnowledgeVerification(
                reliabilityScore = 0.9f,
                duplicateRisk = 0.1f,
                consistencyScore = 0.9f,
                contradictionRisk = 0.1f,
                moderationStatus = ModerationStatus.Safe,
            ),
        )

        val materials = generator.generateMaterials(update)

        assertEquals(3, materials.size)
        assertTrue(materials.all { it.quizQuestions.isNotEmpty() })
        assertTrue(materials.all { it.flashcards.isNotEmpty() })
    }

    private fun collectedKnowledge(
        topic: String,
        text: String = "Educational biology source covers plant energy and classroom examples.",
    ): CollectedKnowledge = CollectedKnowledge(
        task = ResearchTask(
            taskId = "task-$topic",
            topic = topic,
            reason = "Gap detected.",
            priority = ResearchPriority.High,
        ),
        sources = listOf(
            ScientificSource(
                sourceId = "source-1",
                title = "Verified Biology Source",
                url = "internal://biology",
                sourceType = ScientificSourceType.EducationalDatabase,
                reliability = SourceReliability.PeerReviewed,
                publisher = "ROLA",
                topics = listOf("biology", "plant"),
            ),
        ),
        extractedText = text,
    )
}
