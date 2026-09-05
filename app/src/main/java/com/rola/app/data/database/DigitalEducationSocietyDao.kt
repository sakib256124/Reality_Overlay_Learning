package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.DigitalAvatarEntity
import com.rola.app.data.database.entities.EducationInstitutionEntity
import com.rola.app.data.database.entities.GlobalEducationNetworkEntity
import com.rola.app.data.database.entities.GlobalLearningAnalyticsEntity
import com.rola.app.data.database.entities.GovernancePolicyEntity
import com.rola.app.data.database.entities.KnowledgeCommunityEntity
import com.rola.app.data.database.entities.KnowledgeExchangeHistoryEntity
import com.rola.app.data.database.entities.SocietyAIAgentEntity
import com.rola.app.data.database.entities.SocietyInnovationRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DigitalEducationSocietyDao {
    @Query("SELECT * FROM global_education_network WHERE institutionId = :institutionId ORDER BY rowid DESC LIMIT 1")
    fun observeNetwork(institutionId: String): Flow<GlobalEducationNetworkEntity?>

    @Query("SELECT * FROM knowledge_communities ORDER BY rowid DESC LIMIT 10")
    fun observeCommunities(): Flow<List<KnowledgeCommunityEntity>>

    @Query("SELECT * FROM global_learning_analytics WHERE institutionId = :institutionId ORDER BY rowid DESC LIMIT 1")
    fun observeAnalytics(institutionId: String): Flow<GlobalLearningAnalyticsEntity?>

    @Query("SELECT * FROM digital_avatars WHERE learnerId = :learnerId ORDER BY rowid DESC LIMIT 1")
    fun observeAvatar(learnerId: String): Flow<DigitalAvatarEntity?>

    @Query("SELECT * FROM governance_policies ORDER BY rowid DESC LIMIT 10")
    fun observeGovernance(): Flow<List<GovernancePolicyEntity>>

    @Query("SELECT * FROM innovation_records ORDER BY rowid DESC LIMIT 10")
    fun observeInnovation(): Flow<List<SocietyInnovationRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertNetwork(network: GlobalEducationNetworkEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCommunity(community: KnowledgeCommunityEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAgents(agents: List<SocietyAIAgentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertInstitution(institution: EducationInstitutionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertInnovation(record: SocietyInnovationRecordEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAnalytics(report: GlobalLearningAnalyticsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAvatar(avatar: DigitalAvatarEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertGovernance(policy: GovernancePolicyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertExchange(exchange: KnowledgeExchangeHistoryEntity)
}

