package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.AICreativeOutputEntity
import com.rola.app.data.database.entities.ASIModelEntity
import com.rola.app.data.database.entities.ASIProfileEntity
import com.rola.app.data.database.entities.ASIGovernanceRecordEntity
import com.rola.app.data.database.entities.GlobalEducationInsightEntity
import com.rola.app.data.database.entities.HumanAIInteractionEntity
import com.rola.app.data.database.entities.KnowledgeEvolutionRecordEntity
import com.rola.app.data.database.entities.ReasoningHistoryEntity
import com.rola.app.data.database.entities.SelfImprovementLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ASICoreDao {
    @Query("SELECT * FROM reasoning_history ORDER BY rowid DESC LIMIT :limit")
    fun observeReasoningHistory(limit: Int = 10): Flow<List<ReasoningHistoryEntity>>

    @Query("SELECT * FROM ai_creative_outputs ORDER BY rowid DESC LIMIT :limit")
    fun observeCreativeOutputs(limit: Int = 10): Flow<List<AICreativeOutputEntity>>

    @Query("SELECT * FROM asi_governance_records ORDER BY rowid DESC LIMIT :limit")
    fun observeGovernance(limit: Int = 10): Flow<List<ASIGovernanceRecordEntity>>

    @Query("SELECT * FROM global_education_insights WHERE institutionId = :institutionId ORDER BY rowid DESC LIMIT 1")
    fun observeLatestGlobalInsight(institutionId: String): Flow<GlobalEducationInsightEntity?>

    @Query("SELECT * FROM asi_profiles WHERE learnerId = :learnerId ORDER BY updatedAt DESC LIMIT 1")
    fun observeProfile(learnerId: String): Flow<ASIProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProfile(profile: ASIProfileEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertModel(model: ASIModelEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertReasoning(trace: ReasoningHistoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertKnowledge(record: KnowledgeEvolutionRecordEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSelfImprovement(log: SelfImprovementLogEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCreativeOutput(output: AICreativeOutputEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertHumanAIInteraction(interaction: HumanAIInteractionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertGovernance(record: ASIGovernanceRecordEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertGlobalInsight(insight: GlobalEducationInsightEntity)
}
