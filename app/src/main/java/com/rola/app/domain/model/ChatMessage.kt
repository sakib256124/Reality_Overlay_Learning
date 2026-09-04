package com.rola.app.domain.model

enum class ChatRole {
    User,
    Assistant,
    System,
}

data class ChatMessage(
    val messageId: String,
    val userId: String,
    val objectId: String?,
    val role: ChatRole,
    val content: String,
    val timestamp: Long,
    val isGrounded: Boolean = true,
)
