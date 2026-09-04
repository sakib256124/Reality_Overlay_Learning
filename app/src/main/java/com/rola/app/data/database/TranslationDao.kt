package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.LanguageEntity
import com.rola.app.data.database.entities.TranslationCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TranslationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLanguages(languages: List<LanguageEntity>)

    @Query("SELECT * FROM languages ORDER BY languageName ASC")
    fun observeLanguages(): Flow<List<LanguageEntity>>

    @Query("UPDATE languages SET isDownloaded = 1, lastUsedAt = :timestamp WHERE languageCode = :languageCode")
    suspend fun markLanguageDownloaded(languageCode: String, timestamp: Long = System.currentTimeMillis())

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTranslation(cacheEntity: TranslationCacheEntity)

    @Query(
        """
        SELECT * FROM translation_cache
        WHERE cacheId = :cacheId
        LIMIT 1
        """,
    )
    suspend fun getCachedTranslation(cacheId: String): TranslationCacheEntity?

    @Query("DELETE FROM translation_cache WHERE timestamp < :olderThan")
    suspend fun deleteOldTranslations(olderThan: Long)
}

