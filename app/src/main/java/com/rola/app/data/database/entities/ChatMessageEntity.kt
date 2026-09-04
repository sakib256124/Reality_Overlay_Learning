package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.rola.app.domain.model.ChatMessage
import com.rola.app.domain.model.ChatRole

@Entity(
    tableName = "chat_messages",
    indices = [
        Index(value = ["userId"]),
        Index(value = ["objectId"]),
        Index(value = ["timestamp"]),
    ],
)
data class ChatMessageEntity(
    @PrimaryKey val messageId: String,
    val userId: String,
    val objectId: String?,
    val role: ChatRole,
    val content: String,
    val timestamp: Long,
    val isGrounded: Boolean = true,
) {
    fun toDomain(): ChatMessage = ChatMessage(
        messageId = messageId,
        userId = userId,
        objectId = objectId,
        role = role,
        content = content,
        timestamp = timestamp,
        isGrounded = isGrounded,
    )
}

fun ChatMessage.toEntity(): ChatMessageEntity = ChatMessageEntity(
    messageId = messageId,
    userId = userId,
    objectId = objectId,
    role = role,
    content = content,
    timestamp = timestamp,
    isGrounded = isGrounded,
)
