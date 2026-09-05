package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.AICoordinationLogEntity
import com.rola.app.data.database.entities.IntelligenceConnectionEntity
import com.rola.app.data.database.entities.KnowledgeFusionRecordEntity
import com.rola.app.data.database.entities.LearningEvolutionHistoryEntity
import com.rola.app.data.database.entities.SingularityAnalyticsEntity
import com.rola.app.data.database.entities.SingularityGovernanceRecordEntity
import com.rola.app.data.database.entities.UniversalEducationProfileEntity
import com.rola.app.data.database.entities.UniversalLearningModelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EducationSingularityDao {
    @Query("SELECT * FROM universal_learning_models ORDER BY rowid DESC LIMIT 1")
    fun observeModel(): Flow<UniversalLearningModelEntity?>
    @Query("SELECT * FROM knowledge_fusion_records ORDER BY rowid DESC LIMIT 1")
    fun observeFusion(): Flow<KnowledgeFusionRecordEntity?>
    @Query("SELECT * FROM learning_evolution_history ORDER BY rowid DESC LIMIT 1")
    fun observeEvolution(): Flow<LearningEvolutionHistoryEntity?>
    @Query("SELECT * FROM singularity_analytics ORDER BY rowid DESC LIMIT 1")
    fun observeAnalytics(): Flow<SingularityAnalyticsEntity?>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertModel(value: UniversalLearningModelEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertFusion(value: KnowledgeFusionRecordEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertConnection(value: IntelligenceConnectionEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertEvolution(value: LearningEvolutionHistoryEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertProfile(value: UniversalEducationProfileEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertLog(value: AICoordinationLogEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAnalytics(value: SingularityAnalyticsEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertGovernance(value: SingularityGovernanceRecordEntity)
}
