package com.rola.app.data.research

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rola.app.data.database.ResearchDao
import com.rola.app.data.database.entities.toEntity
import com.rola.app.domain.model.ContentVersion
import com.rola.app.domain.model.KnowledgeUpdate
import com.rola.app.domain.model.KnowledgeUpdateStatus
import com.rola.app.domain.model.LearningMaterial
import com.rola.app.domain.model.ResearchDashboardState
import com.rola.app.domain.model.ResearchPriority
import com.rola.app.domain.model.ResearchStatistics
import com.rola.app.domain.model.ResearchTask
import com.rola.app.domain.model.ResearchTaskStatus
import com.rola.app.domain.model.ScientificSource
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.tasks.await

@Singleton
class ResearchRepository @Inject constructor(
    private val researchDao: ResearchDao,
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val researchAgent: ResearchAgent,
    private val sourceManager: SourceManager,
) {
    fun observeDashboard(): Flow<ResearchDashboardState> =
        combine(
            researchDao.observeResearchTasks(),
            researchDao.observeKnowledgeUpdates(),
            researchDao.observeSources(),
            researchDao.observeLearningMaterials(),
        ) { tasks, updates, sources, materials ->
            ResearchDashboardState(
                pendingTasks = tasks.map { it.toDomain() }.filter { it.status != ResearchTaskStatus.Applied },
                pendingUpdates = updates.map { it.toDomain() }.filter { it.status == KnowledgeUpdateStatus.PendingReview },
                trustedSources = sources.map { it.toDomain() }.filter { it.trusted },
                learningMaterials = materials.map { it.toDomain() },
                statistics = ResearchStatistics(
                    pendingTaskCount = tasks.count { it.status == ResearchTaskStatus.Pending },
                    approvalQueueCount = updates.count { it.status == KnowledgeUpdateStatus.PendingReview },
                    trustedSourceCount = sources.count { it.trusted },
                    generatedMaterialCount = materials.size,
                    appliedUpdateCount = updates.count { it.status == KnowledgeUpdateStatus.AppliedToGraph },
                ),
            )
        }

    suspend fun ensureSeedSources() {
        val trusted = researchDao.getTrustedSources()
        if (trusted.isEmpty()) {
            val sources = sourceManager.trustedSeedSources()
            researchDao.upsertSources(sources.map { it.toEntity() })
            uploadSources(sources)
        }
    }

    suspend fun createTaskForTopic(
        topic: String,
        reason: String = "Admin requested scientific expansion.",
        priority: ResearchPriority = ResearchPriority.Medium,
    ): ResearchTask {
        val task = researchAgent.generateResearchTask(topic, reason, priority)
        researchDao.upsertResearchTasks(listOf(task.toEntity()))
        uploadTask(task)
        return task
    }

    suspend fun analyzeGaps(topic: String): List<ResearchTask> {
        ensureSeedSources()
        val tasks = researchAgent.analyzeKnowledgeGaps(topic)
        researchDao.upsertResearchTasks(tasks.map { it.toEntity() })
        tasks.forEach { uploadTask(it) }
        return tasks
    }

    suspend fun runResearchTask(task: ResearchTask): ResearchOutput {
        ensureSeedSources()
        researchDao.updateTaskStatus(task.taskId, ResearchTaskStatus.Analyzing)
        val sources = researchDao.getTrustedSources().map { it.toDomain() }
        val previousUpdates = researchDao.getUpdatesForTopic(task.topic).map { it.toDomain() }
        val output = researchAgent.prepareKnowledgeUpdate(task, sources, previousUpdates)
        researchDao.upsertResearchTasks(listOf(output.task.toEntity()))
        researchDao.upsertKnowledgeUpdates(listOf(output.update.toEntity()))
        researchDao.upsertLearningMaterials(output.learningMaterials.map { it.toEntity() })
        researchDao.upsertContentVersions(output.learningMaterials.map { it.toContentVersion(output.update).toEntity() })
        uploadResearchOutput(output)
        return output
    }

    suspend fun approveUpdate(
        update: KnowledgeUpdate,
        approverId: String = firebaseAuth.currentUser?.uid ?: "local-admin",
    ): KnowledgeUpdate {
        val approved = update.copy(
            status = KnowledgeUpdateStatus.Approved,
            approvedBy = approverId,
            approvedAt = System.currentTimeMillis(),
        )
        researchDao.upsertKnowledgeUpdates(listOf(approved.toEntity()))
        val applied = researchAgent.applyApprovedUpdate(approved)
        researchDao.upsertKnowledgeUpdates(listOf(applied.toEntity()))
        researchDao.updateTaskStatus(applied.taskId, ResearchTaskStatus.Applied)
        uploadUpdate(applied)
        return applied
    }

    suspend fun addSource(source: ScientificSource) {
        researchDao.upsertSources(listOf(source.toEntity()))
        uploadSources(listOf(source))
    }

    suspend fun materialsForTutor(topic: String): List<LearningMaterial> =
        researchDao.getMaterialsForTopic(topic).map { it.toDomain() }

    private suspend fun uploadTask(task: ResearchTask) {
        runCatching {
            firestore.collection(RESEARCH_TASKS).document(task.taskId).set(task.toFirestore()).await()
        }
    }

    private suspend fun uploadSources(sources: List<ScientificSource>) {
        sources.forEach { source ->
            runCatching {
                firestore.collection(SCIENTIFIC_SOURCES).document(source.sourceId).set(source.toFirestore()).await()
            }
        }
    }

    private suspend fun uploadResearchOutput(output: ResearchOutput) {
        uploadTask(output.task)
        uploadUpdate(output.update)
        output.learningMaterials.forEach { material ->
            runCatching {
                firestore.collection(LEARNING_MATERIALS).document(material.materialId).set(material.toFirestore()).await()
            }
        }
    }

    private suspend fun uploadUpdate(update: KnowledgeUpdate) {
        runCatching {
            firestore.collection(KNOWLEDGE_UPDATES).document(update.updateId).set(update.toFirestore()).await()
        }
    }

    private fun LearningMaterial.toContentVersion(update: KnowledgeUpdate): ContentVersion = ContentVersion(
        versionId = "version-${UUID.randomUUID()}",
        contentId = materialId,
        contentType = materialType.name,
        version = version,
        sourceIds = update.sourceIds,
        changeSummary = "Generated from research update ${update.updateId}.",
        modifiedBy = firebaseAuth.currentUser?.uid ?: "research-agent",
    )

    private fun ResearchTask.toFirestore(): Map<String, Any?> = mapOf(
        "taskId" to taskId,
        "topic" to topic,
        "reason" to reason,
        "priority" to priority.name,
        "status" to status.name,
        "suggestedSources" to suggestedSources,
        "assignedTo" to assignedTo,
        "createdAt" to createdAt,
        "updatedAt" to updatedAt,
    )

    private fun ScientificSource.toFirestore(): Map<String, Any?> = mapOf(
        "sourceId" to sourceId,
        "title" to title,
        "url" to url,
        "sourceType" to sourceType.name,
        "reliability" to reliability.name,
        "publisher" to publisher,
        "authors" to authors,
        "publicationYear" to publicationYear,
        "topics" to topics,
        "trusted" to trusted,
        "addedAt" to addedAt,
    )

    private fun KnowledgeUpdate.toFirestore(): Map<String, Any?> = mapOf(
        "updateId" to updateId,
        "taskId" to taskId,
        "topic" to topic,
        "summary" to summary,
        "definitions" to definitions,
        "properties" to properties,
        "applications" to applications,
        "examples" to examples,
        "relationSuggestions" to relationSuggestions.map {
            mapOf(
                "sourceName" to it.sourceName,
                "targetName" to it.targetName,
                "relationType" to it.relationType.wireName,
                "evidence" to it.evidence,
                "confidence" to it.confidence,
            )
        },
        "difficultyLevel" to difficultyLevel.name,
        "sourceIds" to sourceIds,
        "verification" to mapOf(
            "reliabilityScore" to verification.reliabilityScore,
            "duplicateRisk" to verification.duplicateRisk,
            "consistencyScore" to verification.consistencyScore,
            "contradictionRisk" to verification.contradictionRisk,
            "moderationStatus" to verification.moderationStatus.name,
            "notes" to verification.notes,
        ),
        "status" to status.name,
        "version" to version,
        "createdAt" to createdAt,
        "approvedAt" to approvedAt,
        "approvedBy" to approvedBy,
    )

    private fun LearningMaterial.toFirestore(): Map<String, Any?> = mapOf(
        "materialId" to materialId,
        "topic" to topic,
        "materialType" to materialType.name,
        "title" to title,
        "beginnerExplanation" to beginnerExplanation,
        "advancedExplanation" to advancedExplanation,
        "summary" to summary,
        "quizQuestions" to quizQuestions,
        "flashcards" to flashcards.map { mapOf("front" to it.front, "back" to it.back) },
        "sourceUpdateId" to sourceUpdateId,
        "version" to version,
        "status" to status.name,
        "createdAt" to createdAt,
        "updatedAt" to updatedAt,
    )

    private companion object {
        const val RESEARCH_TASKS = "researchTasks"
        const val SCIENTIFIC_SOURCES = "scientificSources"
        const val KNOWLEDGE_UPDATES = "knowledgeUpdates"
        const val LEARNING_MATERIALS = "learningMaterials"
    }
}
