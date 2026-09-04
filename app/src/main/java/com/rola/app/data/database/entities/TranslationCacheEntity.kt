package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.rola.app.domain.model.TranslationResult

@Entity(
    tableName = "translation_cache",
    indices = [
        Index(value = ["sourceLanguage", "targetLanguage"]),
        Index(value = ["timestamp"]),
    ],
)
data class TranslationCacheEntity(
    @PrimaryKey val cacheId: String,
    val sourceText: String,
    val sourceLanguage: String,
    val targetLanguage: String,
    val translatedText: String,
    val timestamp: Long = System.currentTimeMillis(),
) {
    fun toDomain(fromCache: Boolean = true): TranslationResult = TranslationResult(
        cacheId = cacheId,
        sourceText = sourceText,
        sourceLanguage = sourceLanguage,
        targetLanguage = targetLanguage,
        translatedText = translatedText,
        timestamp = timestamp,
        fromCache = fromCache,
    )
}

fun TranslationResult.toEntity(): TranslationCacheEntity = TranslationCacheEntity(
    cacheId = cacheId,
    sourceText = sourceText,
    sourceLanguage = sourceLanguage,
    targetLanguage = targetLanguage,
    translatedText = translatedText,
    timestamp = timestamp,
)

