package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.AGIMemoryEntity
import com.rola.app.data.database.entities.AIDecisionEntity
import com.rola.app.data.database.entities.FutureRecommendationEntity
import com.rola.app.data.database.entities.KnowledgeEvolutionEntity
import com.rola.app.data.database.entities.LearnerModelEntity
import com.rola.app.data.database.entities.LearningGoalEntity
import com.rola.app.data.database.entities.SkillGraphEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AGIDao {
    @Query("SELECT * FROM agi_memory WHERE learnerId = :learnerId ORDER BY timestamp DESC LIMIT :limit")
    suspend fun recentMemory(learnerId: String, limit: Int = 200): List<AGIMemoryEntity>

    @Query("SELECT * FROM agi_memory WHERE learnerId = :learnerId ORDER BY timestamp DESC LIMIT :limit")
    fun observeMemory(learnerId: String, limit: Int = 200): Flow<List<AGIMemoryEntity>>

    @Query("SELECT * FROM learner_models WHERE learnerId = :learnerId")
    suspend fun learnerModel(learnerId: String): LearnerModelEntity?

    @Query("SELECT * FROM skill_graphs WHERE learnerId = :learnerId ORDER BY mastery ASC")
    suspend fun skillGraph(learnerId: String): List<SkillGraphEntity>

    @Query("SELECT * FROM learning_goals WHERE learnerId = :learnerId ORDER BY progress ASC")
    suspend fun learningGoals(learnerId: String): List<LearningGoalEntity>

    @Query("SELECT * FROM ai_decisions ORDER BY createdAt DESC LIMIT :limit")
    fun observeRecentDecisions(limit: Int = 20): Flow<List<AIDecisionEntity>>

    @Query("SELECT * FROM knowledge_evolution WHERE institutionId = :institutionId ORDER BY proposalId DESC LIMIT :limit")
    fun observeKnowledgeEvolution(institutionId: String, limit: Int = 20): Flow<List<KnowledgeEvolutionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMemory(event: AGIMemoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLearnerModel(model: LearnerModelEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSkillGraph(skills: List<SkillGraphEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDecision(decision: AIDecisionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertGoal(goal: LearningGoalEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertFutureRecommendations(recommendations: List<FutureRecommendationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertKnowledgeEvolution(proposal: KnowledgeEvolutionEntity)
}
