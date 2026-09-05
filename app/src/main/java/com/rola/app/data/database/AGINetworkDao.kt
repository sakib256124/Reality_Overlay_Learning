package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.AIEvolutionHistoryEntity
import com.rola.app.data.database.entities.AGINetworkAgentCommunicationEntity
import com.rola.app.data.database.entities.AGINetworkAgentEntity
import com.rola.app.data.database.entities.AGINetworkAgentTaskEntity
import com.rola.app.data.database.entities.AGINetworkAnalyticsEntity
import com.rola.app.data.database.entities.AGINetworkCurriculumEvolutionEntity
import com.rola.app.data.database.entities.AGINetworkDecisionEntity
import com.rola.app.data.database.entities.AGINetworkGovernanceRecordEntity
import com.rola.app.data.database.entities.AGINetworkKnowledgeEvolutionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AGINetworkDao {
    @Query("SELECT * FROM agi_network_ai_decisions ORDER BY rowid DESC LIMIT :limit")
    fun observeRecentDecisions(limit: Int = 20): Flow<List<AGINetworkDecisionEntity>>

    @Query("SELECT * FROM agi_network_agent_tasks ORDER BY priority DESC LIMIT :limit")
    fun observeAgentTasks(limit: Int = 20): Flow<List<AGINetworkAgentTaskEntity>>

    @Query("SELECT * FROM agi_network_governance_records ORDER BY rowid DESC LIMIT :limit")
    fun observeGovernance(limit: Int = 20): Flow<List<AGINetworkGovernanceRecordEntity>>

    @Query("SELECT * FROM agi_network_analytics WHERE institutionId = :institutionId ORDER BY rowid DESC LIMIT 1")
    fun observeLatestAnalytics(institutionId: String): Flow<AGINetworkAnalyticsEntity?>

    @Query("SELECT * FROM agi_network_curriculum_evolution ORDER BY rowid DESC LIMIT :limit")
    fun observeCurriculumEvolution(limit: Int = 20): Flow<List<AGINetworkCurriculumEvolutionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAgents(agents: List<AGINetworkAgentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTasks(tasks: List<AGINetworkAgentTaskEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMessages(messages: List<AGINetworkAgentCommunicationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertEvolutionHistory(history: AIEvolutionHistoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertKnowledgeEvolution(proposal: AGINetworkKnowledgeEvolutionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDecision(decision: AGINetworkDecisionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCurriculumEvolution(plan: AGINetworkCurriculumEvolutionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAnalytics(report: AGINetworkAnalyticsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertGovernance(record: AGINetworkGovernanceRecordEntity)
}
