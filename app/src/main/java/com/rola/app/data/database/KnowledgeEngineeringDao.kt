package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.ConceptMappingEntity
import com.rola.app.data.database.entities.EngineeredKnowledgeAnalyticsEntity
import com.rola.app.data.database.entities.EngineeredKnowledgeEntity
import com.rola.app.data.database.entities.EngineeredKnowledgeEvolutionHistoryEntity
import com.rola.app.data.database.entities.EngineeredKnowledgeRelationshipEntity
import com.rola.app.data.database.entities.EngineeredKnowledgeSourceEntity
import com.rola.app.data.database.entities.EngineeredKnowledgeValidationEntity
import com.rola.app.data.database.entities.EngineeredLearningResourceEntity
import com.rola.app.data.database.entities.SemanticIndexEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KnowledgeEngineeringDao {
    @Query("SELECT * FROM engineered_knowledge_entities ORDER BY rowid DESC LIMIT 1") fun observeEntity(): Flow<EngineeredKnowledgeEntity?>
    @Query("SELECT * FROM engineered_knowledge_sources ORDER BY rowid DESC LIMIT 1") fun observeSource(): Flow<EngineeredKnowledgeSourceEntity?>
    @Query("SELECT * FROM engineered_knowledge_validation ORDER BY rowid DESC LIMIT 1") fun observeValidation(): Flow<EngineeredKnowledgeValidationEntity?>
    @Query("SELECT * FROM engineered_knowledge_evolution_history ORDER BY rowid DESC LIMIT 1") fun observeEvolution(): Flow<EngineeredKnowledgeEvolutionHistoryEntity?>
    @Query("SELECT * FROM engineered_knowledge_analytics ORDER BY rowid DESC LIMIT 1") fun observeAnalytics(): Flow<EngineeredKnowledgeAnalyticsEntity?>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertEntity(value: EngineeredKnowledgeEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertRelationship(value: EngineeredKnowledgeRelationshipEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertSource(value: EngineeredKnowledgeSourceEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertValidation(value: EngineeredKnowledgeValidationEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertMapping(value: ConceptMappingEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertResource(value: EngineeredLearningResourceEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertEvolution(value: EngineeredKnowledgeEvolutionHistoryEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertSemanticIndex(value: SemanticIndexEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAnalytics(value: EngineeredKnowledgeAnalyticsEntity)
}
