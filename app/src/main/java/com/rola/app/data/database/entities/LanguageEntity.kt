package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rola.app.domain.model.Language

@Entity(tableName = "languages")
data class LanguageEntity(
    @PrimaryKey val languageCode: String,
    val languageName: String,
    val nativeName: String,
    val supportedVoice: Boolean,
    val isDownloaded: Boolean = false,
    val lastUsedAt: Long = 0L,
) {
    fun toDomain(): Language = Language(
        languageCode = languageCode,
        languageName = languageName,
        nativeName = nativeName,
        supportedVoice = supportedVoice,
    )
}

fun Language.toEntity(
    isDownloaded: Boolean = false,
    lastUsedAt: Long = 0L,
): LanguageEntity = LanguageEntity(
    languageCode = languageCode,
    languageName = languageName,
    nativeName = nativeName,
    supportedVoice = supportedVoice,
    isDownloaded = isDownloaded,
    lastUsedAt = lastUsedAt,
)

