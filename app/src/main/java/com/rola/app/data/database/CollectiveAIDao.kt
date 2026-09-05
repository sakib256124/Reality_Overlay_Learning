package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.AIConsensusRecordEntity
import com.rola.app.data.database.entities.AgentCommunicationHistoryEntity
import com.rola.app.data.database.entities.CollaborationAnalyticsEntity
import com.rola.app.data.database.entities.CollectiveAIAgentEntity
import com.rola.app.data.database.entities.CollectiveAgentRelationshipEntity
import com.rola.app.data.database.entities.CollectiveAgentTaskEntity
import com.rola.app.data.database.entities.CollectiveKnowledgeExchangeEntity
import com.rola.app.data.database.entities.CollectiveLearningResultEntity
import com.rola.app.data.database.entities.HumanFeedbackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectiveAIDao {
    @Query("SELECT * FROM collective_ai_agents ORDER BY agentType")
    fun observeAgents(): Flow<List<CollectiveAIAgentEntity>>

    @Query("SELECT * FROM collective_knowledge_exchange ORDER BY rowid DESC LIMIT 12")
    fun observeKnowledgeExchange(): Flow<List<CollectiveKnowledgeExchangeEntity>>

    @Query("SELECT * FROM ai_consensus_records ORDER BY rowid DESC LIMIT 1")
    fun observeLatestConsensus(): Flow<AIConsensusRecordEntity?>

    @Query("SELECT * FROM collective_learning_results ORDER BY rowid DESC LIMIT 1")
    fun observeLatestResult(): Flow<CollectiveLearningResultEntity?>

    @Query("SELECT * FROM collaboration_analytics ORDER BY rowid DESC LIMIT 1")
    fun observeLatestAnalytics(): Flow<CollaborationAnalyticsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAgents(agents: List<CollectiveAIAgentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRelationships(relationships: List<CollectiveAgentRelationshipEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTasks(tasks: List<CollectiveAgentTaskEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertKnowledgeExchange(exchanges: List<CollectiveKnowledgeExchangeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertConsensus(consensus: AIConsensusRecordEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCommunications(communications: List<AgentCommunicationHistoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertHumanFeedback(feedback: HumanFeedbackEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLearningResult(result: CollectiveLearningResultEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAnalytics(analytics: CollaborationAnalyticsEntity)
}
