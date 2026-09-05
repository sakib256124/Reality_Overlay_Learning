package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.AICivilizationEntity
import com.rola.app.data.database.entities.AIGovernanceLogEntity
import com.rola.app.data.database.entities.CivilizationAnalyticsEntity
import com.rola.app.data.database.entities.CivilizationInnovationRecordEntity
import com.rola.app.data.database.entities.CivilizationKnowledgeEvolutionEntity
import com.rola.app.data.database.entities.CivilizationLearningEvolutionEntity
import com.rola.app.data.database.entities.FutureEducationPlanEntity
import com.rola.app.data.database.entities.GlobalKnowledgeConnectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AICivilizationDao {
    @Query("SELECT * FROM ai_civilization ORDER BY rowid DESC LIMIT 1") fun observeCivilization(): Flow<AICivilizationEntity?>
    @Query("SELECT * FROM civilization_knowledge_evolution ORDER BY rowid DESC LIMIT 1") fun observeKnowledge(): Flow<CivilizationKnowledgeEvolutionEntity?>
    @Query("SELECT * FROM learning_evolution ORDER BY rowid DESC LIMIT 1") fun observeLearning(): Flow<CivilizationLearningEvolutionEntity?>
    @Query("SELECT * FROM civilization_innovation_records ORDER BY rowid DESC LIMIT 1") fun observeInnovation(): Flow<CivilizationInnovationRecordEntity?>
    @Query("SELECT * FROM future_education_plans ORDER BY rowid DESC LIMIT 1") fun observeFuturePlan(): Flow<FutureEducationPlanEntity?>
    @Query("SELECT * FROM civilization_analytics ORDER BY rowid DESC LIMIT 1") fun observeAnalytics(): Flow<CivilizationAnalyticsEntity?>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertCivilization(value: AICivilizationEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertKnowledge(value: CivilizationKnowledgeEvolutionEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertLearning(value: CivilizationLearningEvolutionEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertInnovation(value: CivilizationInnovationRecordEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertFuturePlan(value: FutureEducationPlanEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertConnection(value: GlobalKnowledgeConnectionEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertGovernance(value: AIGovernanceLogEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAnalytics(value: CivilizationAnalyticsEntity)
}
