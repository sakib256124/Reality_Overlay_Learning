package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.EcosystemAnalyticsEntity
import com.rola.app.data.database.entities.EcosystemOptimizationHistoryEntity
import com.rola.app.data.database.entities.EducationOrchestrationEntity
import com.rola.app.data.database.entities.OrchestrationAIServiceEntity
import com.rola.app.data.database.entities.OrchestrationAgentCoordinationEntity
import com.rola.app.data.database.entities.QualityMetricEntity
import com.rola.app.data.database.entities.SystemDecisionEntity
import com.rola.app.data.database.entities.WorkflowProcessEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EducationOrchestrationDao {
    @Query("SELECT * FROM education_orchestration ORDER BY rowid DESC LIMIT 1") fun observeOrchestration(): Flow<EducationOrchestrationEntity?>
    @Query("SELECT * FROM orchestration_ai_services ORDER BY rowid DESC LIMIT 1") fun observeServices(): Flow<OrchestrationAIServiceEntity?>
    @Query("SELECT * FROM workflow_processes ORDER BY rowid DESC LIMIT 1") fun observeWorkflow(): Flow<WorkflowProcessEntity?>
    @Query("SELECT * FROM agent_coordination ORDER BY rowid DESC LIMIT 1") fun observeAgentCoordination(): Flow<OrchestrationAgentCoordinationEntity?>
    @Query("SELECT * FROM system_decisions ORDER BY rowid DESC LIMIT 1") fun observeDecision(): Flow<SystemDecisionEntity?>
    @Query("SELECT * FROM ecosystem_optimization_history ORDER BY rowid DESC LIMIT 1") fun observeOptimization(): Flow<EcosystemOptimizationHistoryEntity?>
    @Query("SELECT * FROM quality_metrics ORDER BY rowid DESC LIMIT 1") fun observeQuality(): Flow<QualityMetricEntity?>
    @Query("SELECT * FROM ecosystem_analytics ORDER BY rowid DESC LIMIT 1") fun observeAnalytics(): Flow<EcosystemAnalyticsEntity?>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertOrchestration(value: EducationOrchestrationEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertServices(value: OrchestrationAIServiceEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertWorkflow(value: WorkflowProcessEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAgentCoordination(value: OrchestrationAgentCoordinationEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertDecision(value: SystemDecisionEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertOptimization(value: EcosystemOptimizationHistoryEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertQuality(value: QualityMetricEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAnalytics(value: EcosystemAnalyticsEntity)
}
