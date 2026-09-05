package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "ai_research_projects", indices = [Index(value = ["domain"])])
data class AIResearchProjectEntity(@PrimaryKey val projectId: String, val title: String, val domain: String, val roadmap: List<String>)
@Entity(tableName = "ai_research_ideas", indices = [Index(value = ["discoveryId"])])
data class AIResearchIdeaEntity(@PrimaryKey val discoveryId: String, val knowledgeGaps: List<String>, val opportunities: List<String>, val emergingTopics: List<String>)
@Entity(tableName = "hypotheses", indices = [Index(value = ["hypothesisId"])])
data class HypothesisEntity(@PrimaryKey val hypothesisId: String, val questions: List<String>, val hypotheses: List<String>, val possibleSolutions: List<String>)
@Entity(tableName = "experiments", indices = [Index(value = ["experimentId"])])
data class ExperimentEntity(@PrimaryKey val experimentId: String, val procedures: List<String>, val resources: List<String>, val simulationPlans: List<String>, val expectedResults: List<String>)
@Entity(tableName = "research_results", indices = [Index(value = ["resultId"])])
data class ResearchResultEntity(@PrimaryKey val resultId: String, val patterns: List<String>, val relationships: List<String>, val interpretation: String)
@Entity(tableName = "validation_records", indices = [Index(value = ["approved"])])
data class ValidationRecordEntity(@PrimaryKey val validationId: String, val accuracyScore: Int, val sourceReliability: Int, val approved: Boolean, val reasoning: String)
@Entity(tableName = "scientific_knowledge", indices = [Index(value = ["packageId"])])
data class ScientificKnowledgeEntity(@PrimaryKey val packageId: String, val lessons: List<String>, val tutorials: List<String>, val simulations: List<String>, val projects: List<String>)
@Entity(tableName = "research_collaborations", indices = [Index(value = ["collaborationId"])])
data class ResearchCollaborationEntity(@PrimaryKey val collaborationId: String, val participants: List<String>, val sharedProjects: List<String>, val discussionSummary: String)
@Entity(tableName = "research_analytics", indices = [Index(value = ["validationScore"])])
data class AIResearchAnalyticsEntity(@PrimaryKey val analyticsId: String, val discoveryScore: Int, val validationScore: Int, val knowledgeGrowth: Int)
