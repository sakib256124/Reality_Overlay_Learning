package com.rola.app.personal_agent.agent_core

enum class AgentCapability { Teaching, Tutoring, Planning, Research, Prediction, EmotionalSupport, Mastery, Mentoring }
enum class AgentStatus { Initializing, Active, Evolving, NeedsHumanReview }

data class AgentRequest(
    val userId: String,
    val userNeed: String,
    val currentGoal: String,
    val skillLevel: String,
    val emotionState: String,
    val learningSpeed: String,
    val previousMistakes: List<String>,
    val careerObjective: String,
)

data class PersonalEducationAgent(
    val agentId: String,
    val userId: String,
    val learningHistory: List<String>,
    val knowledgeProfile: List<String>,
    val skills: List<String>,
    val goals: List<String>,
    val preferences: List<String>,
    val personality: String,
    val learningStyle: String,
    val careerObjectives: List<String>,
    val status: AgentStatus,
)

data class AgentIntelligencePlan(val planId: String, val selectedCapabilities: List<AgentCapability>, val responseStrategy: String, val transparentDecision: String)
data class AgentMemorySnapshot(val memoryId: String, val shortTermMemory: List<String>, val longTermMemory: List<String>, val userControlled: Boolean, val privacyProtected: Boolean)
data class AgentTeachingResponse(val teachingId: String, val explanation: String, val examples: List<String>, val exercises: List<String>, val evaluationPrompt: String)
data class AgentMentorPlan(val mentorId: String, val careerGuidance: List<String>, val researchGuidance: List<String>, val skillDevelopment: List<String>, val growthRoadmap: List<String>)
data class AgentDecision(val decisionId: String, val moduleToUse: String, val explanationStyle: String, val learningActivity: String, val strategy: String, val humanControl: Boolean)
data class AgentEvolutionRecord(val evolutionId: String, val teachingImprovement: String, val communicationStyle: String, val recommendationImprovement: String, val planningAccuracy: Int, val personalUnderstanding: Int)
data class UniversalAgentResult(
    val resultId: String,
    val agent: PersonalEducationAgent,
    val intelligence: AgentIntelligencePlan,
    val memory: AgentMemorySnapshot,
    val teaching: AgentTeachingResponse,
    val mentor: AgentMentorPlan,
    val decision: AgentDecision,
    val evolution: AgentEvolutionRecord,
)
