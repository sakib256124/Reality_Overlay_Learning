package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.ChatMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatMessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity)

    @Query(
        """
        SELECT * FROM chat_messages
        WHERE userId = :userId
            AND (:objectId IS NULL OR objectId = :objectId)
        ORDER BY timestamp ASC
        """,
    )
    fun observeMessages(userId: String, objectId: String?): Flow<List<ChatMessageEntity>>

    @Query(
        """
        SELECT * FROM chat_messages
        WHERE userId = :userId
            AND (:objectId IS NULL OR objectId = :objectId)
        ORDER BY timestamp DESC
        LIMIT :limit
        """,
    )
    suspend fun getRecentMessages(
        userId: String,
        objectId: String?,
        limit: Int,
    ): List<ChatMessageEntity>

    @Query("SELECT * FROM chat_messages WHERE userId = :userId ORDER BY timestamp DESC")
    suspend fun getUserMessagesSnapshot(userId: String): List<ChatMessageEntity>

    @Query("DELETE FROM chat_messages WHERE userId = :userId AND (:objectId IS NULL OR objectId = :objectId)")
    suspend fun clearMessages(userId: String, objectId: String?)
}
