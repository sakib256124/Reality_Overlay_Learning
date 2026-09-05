package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.AIResearchAnalyticsEntity
import com.rola.app.data.database.entities.AIResearchIdeaEntity
import com.rola.app.data.database.entities.AIResearchProjectEntity
import com.rola.app.data.database.entities.ExperimentEntity
import com.rola.app.data.database.entities.HypothesisEntity
import com.rola.app.data.database.entities.ResearchCollaborationEntity
import com.rola.app.data.database.entities.ResearchResultEntity
import com.rola.app.data.database.entities.ScientificKnowledgeEntity
import com.rola.app.data.database.entities.ValidationRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AIResearchDao {
    @Query("SELECT * FROM ai_research_projects ORDER BY rowid DESC LIMIT 1") fun observeProject(): Flow<AIResearchProjectEntity?>
    @Query("SELECT * FROM ai_research_ideas ORDER BY rowid DESC LIMIT 1") fun observeDiscovery(): Flow<AIResearchIdeaEntity?>
    @Query("SELECT * FROM hypotheses ORDER BY rowid DESC LIMIT 1") fun observeHypotheses(): Flow<HypothesisEntity?>
    @Query("SELECT * FROM experiments ORDER BY rowid DESC LIMIT 1") fun observeExperiment(): Flow<ExperimentEntity?>
    @Query("SELECT * FROM research_analytics ORDER BY rowid DESC LIMIT 1") fun observeAnalytics(): Flow<AIResearchAnalyticsEntity?>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertProject(value: AIResearchProjectEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertDiscovery(value: AIResearchIdeaEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertHypotheses(value: HypothesisEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertExperiment(value: ExperimentEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertResult(value: ResearchResultEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertValidation(value: ValidationRecordEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertKnowledge(value: ScientificKnowledgeEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertCollaboration(value: ResearchCollaborationEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAnalytics(value: AIResearchAnalyticsEntity)
}
