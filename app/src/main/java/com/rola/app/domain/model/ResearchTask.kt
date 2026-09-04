package com.rola.app.domain.model

data class ResearchTask(
    val taskId: String,
    val topic: String,
    val reason: String,
    val priority: ResearchPriority,
    val status: ResearchTaskStatus = ResearchTaskStatus.Pending,
    val suggestedSources: List<String> = emptyList(),
    val assignedTo: String = "ai-research-agent",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)

enum class ResearchPriority {
    Low,
    Medium,
    High,
}

enum class ResearchTaskStatus {
    Pending,
    Collecting,
    Analyzing,
    AwaitingApproval,
    Approved,
    Rejected,
    Applied,
}
