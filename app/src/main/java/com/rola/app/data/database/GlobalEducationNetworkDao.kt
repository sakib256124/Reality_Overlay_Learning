package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.GlobalNetworkAnalyticsEntity
import com.rola.app.data.database.entities.GlobalNetworkCollaborationProjectEntity
import com.rola.app.data.database.entities.GlobalNetworkCourseEntity
import com.rola.app.data.database.entities.GlobalNetworkInstitutionEntity
import com.rola.app.data.database.entities.GlobalNetworkInternationalOpportunityEntity
import com.rola.app.data.database.entities.GlobalNetworkKnowledgeExchangeRecordEntity
import com.rola.app.data.database.entities.GlobalNetworkResearchNetworkEntity
import com.rola.app.data.database.entities.GlobalNetworkUserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GlobalEducationNetworkDao {
    @Query("SELECT * FROM global_network_users ORDER BY rowid DESC LIMIT 1") fun observeUser(): Flow<GlobalNetworkUserEntity?>
    @Query("SELECT * FROM global_network_institutions ORDER BY rowid DESC LIMIT 1") fun observeInstitutions(): Flow<GlobalNetworkInstitutionEntity?>
    @Query("SELECT * FROM global_network_collaboration_projects ORDER BY rowid DESC LIMIT 1") fun observeCollaboration(): Flow<GlobalNetworkCollaborationProjectEntity?>
    @Query("SELECT * FROM global_network_knowledge_exchange_records ORDER BY rowid DESC LIMIT 1") fun observeExchange(): Flow<GlobalNetworkKnowledgeExchangeRecordEntity?>
    @Query("SELECT * FROM global_network_courses ORDER BY rowid DESC LIMIT 1") fun observeCourses(): Flow<GlobalNetworkCourseEntity?>
    @Query("SELECT * FROM global_network_research_networks ORDER BY rowid DESC LIMIT 1") fun observeResearch(): Flow<GlobalNetworkResearchNetworkEntity?>
    @Query("SELECT * FROM global_network_international_opportunities ORDER BY rowid DESC LIMIT 1") fun observeOpportunities(): Flow<GlobalNetworkInternationalOpportunityEntity?>
    @Query("SELECT * FROM global_network_analytics ORDER BY rowid DESC LIMIT 1") fun observeAnalytics(): Flow<GlobalNetworkAnalyticsEntity?>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertUser(value: GlobalNetworkUserEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertInstitutions(value: GlobalNetworkInstitutionEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertCollaboration(value: GlobalNetworkCollaborationProjectEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertExchange(value: GlobalNetworkKnowledgeExchangeRecordEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertCourses(value: GlobalNetworkCourseEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertResearch(value: GlobalNetworkResearchNetworkEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertOpportunities(value: GlobalNetworkInternationalOpportunityEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAnalytics(value: GlobalNetworkAnalyticsEntity)
}
