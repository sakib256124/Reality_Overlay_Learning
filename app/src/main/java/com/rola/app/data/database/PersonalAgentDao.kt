package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.AgentAnalyticsEntity
import com.rola.app.data.database.entities.AgentEvolutionHistoryEntity
import com.rola.app.data.database.entities.AgentInteractionEntity
import com.rola.app.data.database.entities.AgentLearningHistoryEntity
import com.rola.app.data.database.entities.AgentMemoryEntity
import com.rola.app.data.database.entities.AgentProfileEntity
import com.rola.app.data.database.entities.AgentRecommendationEntity
import com.rola.app.data.database.entities.PersonalAgentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonalAgentDao {
    @Query("SELECT * FROM personal_agents ORDER BY rowid DESC LIMIT 1") fun observeAgent(): Flow<PersonalAgentEntity?>
    @Query("SELECT * FROM agent_profiles ORDER BY rowid DESC LIMIT 1") fun observeProfile(): Flow<AgentProfileEntity?>
    @Query("SELECT * FROM agent_memory ORDER BY rowid DESC LIMIT 1") fun observeMemory(): Flow<AgentMemoryEntity?>
    @Query("SELECT * FROM agent_interactions ORDER BY rowid DESC LIMIT 1") fun observeInteraction(): Flow<AgentInteractionEntity?>
    @Query("SELECT * FROM agent_learning_history ORDER BY rowid DESC LIMIT 1") fun observeLearningHistory(): Flow<AgentLearningHistoryEntity?>
    @Query("SELECT * FROM agent_recommendations ORDER BY rowid DESC LIMIT 1") fun observeRecommendation(): Flow<AgentRecommendationEntity?>
    @Query("SELECT * FROM agent_evolution_history ORDER BY rowid DESC LIMIT 1") fun observeEvolution(): Flow<AgentEvolutionHistoryEntity?>
    @Query("SELECT * FROM agent_analytics ORDER BY rowid DESC LIMIT 1") fun observeAnalytics(): Flow<AgentAnalyticsEntity?>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAgent(value: PersonalAgentEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertProfile(value: AgentProfileEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertMemory(value: AgentMemoryEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertInteraction(value: AgentInteractionEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertLearningHistory(value: AgentLearningHistoryEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertRecommendation(value: AgentRecommendationEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertEvolution(value: AgentEvolutionHistoryEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAnalytics(value: AgentAnalyticsEntity)
}
