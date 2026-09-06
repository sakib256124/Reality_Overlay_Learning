package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.NeuralLearningAdaptationHistoryEntity
import com.rola.app.data.database.entities.NeuralLearningCognitiveAnalyticsEntity
import com.rola.app.data.database.entities.NeuralLearningCognitiveModelEntity
import com.rola.app.data.database.entities.NeuralLearningKnowledgePathwayEntity
import com.rola.app.data.database.entities.NeuralLearningMemoryNetworkEntity
import com.rola.app.data.database.entities.NeuralLearningPatternEntity
import com.rola.app.data.database.entities.NeuralLearningProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NeuralLearningAIDao {
    @Query("SELECT * FROM neural_learning_profiles ORDER BY rowid DESC LIMIT 1") fun observeProfile(): Flow<NeuralLearningProfileEntity?>
    @Query("SELECT * FROM neural_learning_cognitive_models ORDER BY rowid DESC LIMIT 1") fun observeCognitiveModel(): Flow<NeuralLearningCognitiveModelEntity?>
    @Query("SELECT * FROM neural_learning_knowledge_pathways ORDER BY rowid DESC LIMIT 1") fun observePathway(): Flow<NeuralLearningKnowledgePathwayEntity?>
    @Query("SELECT * FROM neural_learning_memory_networks ORDER BY rowid DESC LIMIT 1") fun observeMemory(): Flow<NeuralLearningMemoryNetworkEntity?>
    @Query("SELECT * FROM neural_learning_patterns ORDER BY rowid DESC LIMIT 1") fun observePattern(): Flow<NeuralLearningPatternEntity?>
    @Query("SELECT * FROM neural_learning_cognitive_analytics ORDER BY rowid DESC LIMIT 1") fun observeAnalytics(): Flow<NeuralLearningCognitiveAnalyticsEntity?>
    @Query("SELECT * FROM neural_learning_adaptation_history ORDER BY rowid DESC LIMIT 1") fun observeAdaptation(): Flow<NeuralLearningAdaptationHistoryEntity?>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertProfile(value: NeuralLearningProfileEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertCognitiveModel(value: NeuralLearningCognitiveModelEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertPathway(value: NeuralLearningKnowledgePathwayEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertMemory(value: NeuralLearningMemoryNetworkEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertPattern(value: NeuralLearningPatternEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAnalytics(value: NeuralLearningCognitiveAnalyticsEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAdaptation(value: NeuralLearningAdaptationHistoryEntity)
}
