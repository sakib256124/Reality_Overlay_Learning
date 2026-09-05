package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.AIClusterEntity
import com.rola.app.data.database.entities.AIInfrastructureEntity
import com.rola.app.data.database.entities.CloudServiceEntity
import com.rola.app.data.database.entities.DeploymentHistoryEntity
import com.rola.app.data.database.entities.EdgeDeviceEntity
import com.rola.app.data.database.entities.ModelRegistryEntity
import com.rola.app.data.database.entities.ResourceMetricEntity
import com.rola.app.data.database.entities.ScalingEventEntity
import com.rola.app.data.database.entities.SystemHealthEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AIInfrastructureDao {
    @Query("SELECT * FROM ai_infrastructure WHERE institutionId = :institutionId ORDER BY rowid DESC LIMIT 1")
    fun observeInfrastructure(institutionId: String): Flow<AIInfrastructureEntity?>

    @Query("SELECT * FROM cloud_services ORDER BY rowid DESC LIMIT 10")
    fun observeCloudServices(): Flow<List<CloudServiceEntity>>

    @Query("SELECT * FROM ai_clusters ORDER BY rowid DESC LIMIT 1")
    fun observeLatestCluster(): Flow<AIClusterEntity?>

    @Query("SELECT * FROM resource_metrics ORDER BY rowid DESC LIMIT 1")
    fun observeLatestResourceMetric(): Flow<ResourceMetricEntity?>

    @Query("SELECT * FROM system_health ORDER BY rowid DESC LIMIT 1")
    fun observeLatestHealth(): Flow<SystemHealthEntity?>

    @Query("SELECT * FROM scaling_events WHERE institutionId = :institutionId ORDER BY createdAt DESC LIMIT 10")
    fun observeScalingEvents(institutionId: String): Flow<List<ScalingEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertInfrastructure(infrastructure: AIInfrastructureEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCloudServices(services: List<CloudServiceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertEdgeDevices(devices: List<EdgeDeviceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCluster(cluster: AIClusterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertModels(models: List<ModelRegistryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDeployment(history: DeploymentHistoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertResourceMetric(metric: ResourceMetricEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertHealth(report: SystemHealthEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertScalingEvent(event: ScalingEventEntity)
}

