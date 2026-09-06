package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.FutureKnowledgeDiscoveryModelEntity
import com.rola.app.data.database.entities.GlobalKnowledgeSourceEntity
import com.rola.app.data.database.entities.KnowledgeDiscoveryEntity
import com.rola.app.data.database.entities.KnowledgeDiscoveryIntelligenceNetworkEntity
import com.rola.app.data.database.entities.KnowledgeDiscoveryRelationshipMapEntity
import com.rola.app.data.database.entities.KnowledgeDiscoveryValidationEntity
import com.rola.app.data.database.entities.KnowledgeResearchOpportunityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KnowledgeDiscoveryAIDao {
    @Query("SELECT * FROM knowledge_discovery_discoveries ORDER BY rowid DESC LIMIT 1") fun observeDiscovery(): Flow<KnowledgeDiscoveryEntity?>
    @Query("SELECT * FROM knowledge_discovery_global_sources ORDER BY rowid DESC LIMIT 1") fun observeSources(): Flow<GlobalKnowledgeSourceEntity?>
    @Query("SELECT * FROM knowledge_discovery_research_opportunities ORDER BY rowid DESC LIMIT 1") fun observeOpportunities(): Flow<KnowledgeResearchOpportunityEntity?>
    @Query("SELECT * FROM knowledge_discovery_relationship_maps ORDER BY rowid DESC LIMIT 1") fun observeRelationships(): Flow<KnowledgeDiscoveryRelationshipMapEntity?>
    @Query("SELECT * FROM knowledge_discovery_validation ORDER BY rowid DESC LIMIT 1") fun observeValidation(): Flow<KnowledgeDiscoveryValidationEntity?>
    @Query("SELECT * FROM knowledge_discovery_future_models ORDER BY rowid DESC LIMIT 1") fun observeFutureModel(): Flow<FutureKnowledgeDiscoveryModelEntity?>
    @Query("SELECT * FROM knowledge_discovery_intelligence_network ORDER BY rowid DESC LIMIT 1") fun observeNetwork(): Flow<KnowledgeDiscoveryIntelligenceNetworkEntity?>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertDiscovery(value: KnowledgeDiscoveryEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertSources(value: GlobalKnowledgeSourceEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertOpportunities(value: KnowledgeResearchOpportunityEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertRelationships(value: KnowledgeDiscoveryRelationshipMapEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertValidation(value: KnowledgeDiscoveryValidationEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertFutureModel(value: FutureKnowledgeDiscoveryModelEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertNetwork(value: KnowledgeDiscoveryIntelligenceNetworkEntity)
}
