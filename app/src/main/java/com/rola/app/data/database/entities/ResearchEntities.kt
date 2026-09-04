package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.rola.app.domain.model.ContentApprovalStatus
import com.rola.app.domain.model.ContentVersion
import com.rola.app.domain.model.Flashcard
import com.rola.app.domain.model.KnowledgeRelationType
import com.rola.app.domain.model.KnowledgeUpdate
import com.rola.app.domain.model.KnowledgeUpdateStatus
import com.rola.app.domain.model.KnowledgeVerification
import com.rola.app.domain.model.LearningMaterial
import com.rola.app.domain.model.LearningMaterialType
import com.rola.app.domain.model.ModerationStatus
import com.rola.app.domain.model.RelationSuggestion
import com.rola.app.domain.model.ResearchPriority
import com.rola.app.domain.model.ResearchTask
import com.rola.app.domain.model.ResearchTaskStatus
import com.rola.app.domain.model.ScientificSource
import com.rola.app.domain.model.ScientificSourceType
import com.rola.app.domain.model.SkillLevel
import com.rola.app.domain.model.SourceReliability

@Entity(
    tableName = "research_tasks",
    indices = [Index(value = ["topic"]), Index(value = ["status"]), Index(value = ["priority"])],
)
data class ResearchTaskEntity(
    @PrimaryKey val taskId: String,
    val topic: String,
    val reason: String,
    val priority: ResearchPriority,
    val status: ResearchTaskStatus,
    val suggestedSources: List<String>,
    val assignedTo: String,
    val createdAt: Long,
    val updatedAt: Long,
) {
    fun toDomain(): ResearchTask = ResearchTask(
        taskId = taskId,
        topic = topic,
        reason = reason,
        priority = priority,
        status = status,
        suggestedSources = suggestedSources,
        assignedTo = assignedTo,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}

@Entity(
    tableName = "scientific_sources",
    indices = [Index(value = ["sourceType"]), Index(value = ["reliability"]), Index(value = ["trusted"])],
)
data class ScientificSourceEntity(
    @PrimaryKey val sourceId: String,
    val title: String,
    val url: String,
    val sourceType: ScientificSourceType,
    val reliability: SourceReliability,
    val publisher: String,
    val authors: List<String>,
    val publicationYear: Int?,
    val topics: List<String>,
    val trusted: Boolean,
    val addedAt: Long,
) {
    fun toDomain(): ScientificSource = ScientificSource(
        sourceId = sourceId,
        title = title,
        url = url,
        sourceType = sourceType,
        reliability = reliability,
        publisher = publisher,
        authors = authors,
        publicationYear = publicationYear,
        topics = topics,
        trusted = trusted,
        addedAt = addedAt,
    )
}

@Entity(
    tableName = "knowledge_updates",
    indices = [Index(value = ["taskId"]), Index(value = ["topic"]), Index(value = ["status"]), Index(value = ["createdAt"])],
)
data class KnowledgeUpdateEntity(
    @PrimaryKey val updateId: String,
    val taskId: String,
    val topic: String,
    val summary: String,
    val definitions: List<String>,
    val properties: List<String>,
    val applications: List<String>,
    val examples: List<String>,
    val relationSuggestions: List<String>,
    val difficultyLevel: SkillLevel,
    val sourceIds: List<String>,
    val reliabilityScore: Float,
    val duplicateRisk: Float,
    val consistencyScore: Float,
    val contradictionRisk: Float,
    val moderationStatus: ModerationStatus,
    val verificationNotes: List<String>,
    val status: KnowledgeUpdateStatus,
    val version: Int,
    val createdAt: Long,
    val approvedAt: Long?,
    val approvedBy: String?,
) {
    fun toDomain(): KnowledgeUpdate = KnowledgeUpdate(
        updateId = updateId,
        taskId = taskId,
        topic = topic,
        summary = summary,
        definitions = definitions,
        properties = properties,
        applications = applications,
        examples = examples,
        relationSuggestions = relationSuggestions.mapNotNull { it.toRelationSuggestionOrNull() },
        difficultyLevel = difficultyLevel,
        sourceIds = sourceIds,
        verification = KnowledgeVerification(
            reliabilityScore = reliabilityScore,
            duplicateRisk = duplicateRisk,
            consistencyScore = consistencyScore,
            contradictionRisk = contradictionRisk,
            moderationStatus = moderationStatus,
            notes = verificationNotes,
        ),
        status = status,
        version = version,
        createdAt = createdAt,
        approvedAt = approvedAt,
        approvedBy = approvedBy,
    )
}

@Entity(
    tableName = "learning_materials",
    indices = [Index(value = ["topic"]), Index(value = ["materialType"]), Index(value = ["status"])],
)
data class LearningMaterialEntity(
    @PrimaryKey val materialId: String,
    val topic: String,
    val materialType: LearningMaterialType,
    val title: String,
    val beginnerExplanation: String,
    val advancedExplanation: String,
    val summary: String,
    val quizQuestions: List<String>,
    val flashcards: List<String>,
    val sourceUpdateId: String,
    val version: Int,
    val status: ContentApprovalStatus,
    val createdAt: Long,
    val updatedAt: Long,
) {
    fun toDomain(): LearningMaterial = LearningMaterial(
        materialId = materialId,
        topic = topic,
        materialType = materialType,
        title = title,
        beginnerExplanation = beginnerExplanation,
        advancedExplanation = advancedExplanation,
        summary = summary,
        quizQuestions = quizQuestions,
        flashcards = flashcards.mapNotNull { it.toFlashcardOrNull() },
        sourceUpdateId = sourceUpdateId,
        version = version,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}

@Entity(
    tableName = "content_versions",
    indices = [Index(value = ["contentId"]), Index(value = ["contentType"]), Index(value = ["createdAt"])],
)
data class ContentVersionEntity(
    @PrimaryKey val versionId: String,
    val contentId: String,
    val contentType: String,
    val version: Int,
    val sourceIds: List<String>,
    val changeSummary: String,
    val modifiedBy: String,
    val createdAt: Long,
) {
    fun toDomain(): ContentVersion = ContentVersion(
        versionId = versionId,
        contentId = contentId,
        contentType = contentType,
        version = version,
        sourceIds = sourceIds,
        changeSummary = changeSummary,
        modifiedBy = modifiedBy,
        createdAt = createdAt,
    )
}

fun ResearchTask.toEntity(): ResearchTaskEntity = ResearchTaskEntity(
    taskId = taskId,
    topic = topic,
    reason = reason,
    priority = priority,
    status = status,
    suggestedSources = suggestedSources,
    assignedTo = assignedTo,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun ScientificSource.toEntity(): ScientificSourceEntity = ScientificSourceEntity(
    sourceId = sourceId,
    title = title,
    url = url,
    sourceType = sourceType,
    reliability = reliability,
    publisher = publisher,
    authors = authors,
    publicationYear = publicationYear,
    topics = topics,
    trusted = trusted,
    addedAt = addedAt,
)

fun KnowledgeUpdate.toEntity(): KnowledgeUpdateEntity = KnowledgeUpdateEntity(
    updateId = updateId,
    taskId = taskId,
    topic = topic,
    summary = summary,
    definitions = definitions,
    properties = properties,
    applications = applications,
    examples = examples,
    relationSuggestions = relationSuggestions.map { it.toStorageValue() },
    difficultyLevel = difficultyLevel,
    sourceIds = sourceIds,
    reliabilityScore = verification.reliabilityScore,
    duplicateRisk = verification.duplicateRisk,
    consistencyScore = verification.consistencyScore,
    contradictionRisk = verification.contradictionRisk,
    moderationStatus = verification.moderationStatus,
    verificationNotes = verification.notes,
    status = status,
    version = version,
    createdAt = createdAt,
    approvedAt = approvedAt,
    approvedBy = approvedBy,
)

fun LearningMaterial.toEntity(): LearningMaterialEntity = LearningMaterialEntity(
    materialId = materialId,
    topic = topic,
    materialType = materialType,
    title = title,
    beginnerExplanation = beginnerExplanation,
    advancedExplanation = advancedExplanation,
    summary = summary,
    quizQuestions = quizQuestions,
    flashcards = flashcards.map { "${it.front}|${it.back}" },
    sourceUpdateId = sourceUpdateId,
    version = version,
    status = status,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun ContentVersion.toEntity(): ContentVersionEntity = ContentVersionEntity(
    versionId = versionId,
    contentId = contentId,
    contentType = contentType,
    version = version,
    sourceIds = sourceIds,
    changeSummary = changeSummary,
    modifiedBy = modifiedBy,
    createdAt = createdAt,
)

private fun RelationSuggestion.toStorageValue(): String =
    listOf(sourceName, targetName, relationType.name, confidence.toString(), evidence).joinToString(separator = "|")

private fun String.toRelationSuggestionOrNull(): RelationSuggestion? {
    val parts = split("|", limit = 5)
    if (parts.size < 5) return null
    return RelationSuggestion(
        sourceName = parts[0],
        targetName = parts[1],
        relationType = runCatching { KnowledgeRelationType.valueOf(parts[2]) }.getOrDefault(KnowledgeRelationType.RelatedTo),
        confidence = parts[3].toFloatOrNull() ?: 0.5f,
        evidence = parts[4],
    )
}

private fun String.toFlashcardOrNull(): Flashcard? {
    val parts = split("|", limit = 2)
    if (parts.size < 2) return null
    return Flashcard(front = parts[0], back = parts[1])
}
