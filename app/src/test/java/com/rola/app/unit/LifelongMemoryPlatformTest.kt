package com.rola.app.unit

import com.rola.app.lifelong_memory.evolution.MemoryEvolutionManager
import com.rola.app.lifelong_memory.experience.ExperienceMemoryManager
import com.rola.app.lifelong_memory.intelligence.LifelongMentorAgent
import com.rola.app.lifelong_memory.intelligence.PersonalKnowledgeGraph
import com.rola.app.lifelong_memory.knowledge_storage.KnowledgeStorageManager
import com.rola.app.lifelong_memory.memory_core.LearningMemoryCore
import com.rola.app.lifelong_memory.memory_core.LifelongMemoryContext
import com.rola.app.lifelong_memory.memory_core.LifelongMemoryEngine
import com.rola.app.lifelong_memory.retrieval.MemoryRetrievalEngine
import org.junit.Assert.assertTrue
import org.junit.Test

class LifelongMemoryPlatformTest {
    private val engine = LifelongMemoryEngine(
        LearningMemoryCore(),
        KnowledgeStorageManager(),
        ExperienceMemoryManager(),
        MemoryRetrievalEngine(),
        MemoryEvolutionManager(),
        PersonalKnowledgeGraph(),
        LifelongMentorAgent(),
    )

    @Test
    fun memoryCycle_buildsOwnedMemoryGraphRetrievalMentorAndSecurity() {
        val result = engine.updateMemory(
            LifelongMemoryContext(
                userId = "learner-memory",
                currentLesson = "Electric Circuits",
                activeGoal = "Master current flow",
                recentInteractions = listOf("I confuse voltage and current"),
                completedProjects = listOf("AR circuit lab"),
                achievements = listOf("Basics badge", "Quiz streak", "Project demo"),
            ),
        )

        assertTrue(result.memory.userOwned)
        assertTrue(result.graph.concepts.contains("Electric Circuits"))
        assertTrue(result.retrieval.pastMistakes.isNotEmpty())
        assertTrue(result.evolution.newConnections.isNotEmpty())
        assertTrue(result.mentorPlan.futureRoadmap.isNotEmpty())
        assertTrue(result.security.encrypted)
        assertTrue(result.analytics.personalizationScore >= 90)
    }
}
