package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.AIOSConfigEntity
import com.rola.app.data.database.entities.AIOSServiceEntity
import com.rola.app.data.database.entities.AgentRegistryEntity
import com.rola.app.data.database.entities.ExtensionRegistryEntity
import com.rola.app.data.database.entities.MemoryCoreEntity
import com.rola.app.data.database.entities.ResourceRegistryEntity
import com.rola.app.data.database.entities.SecurityLogEntity
import com.rola.app.data.database.entities.SystemEventEntity
import com.rola.app.data.database.entities.WorkflowHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AIOSCoreDao {
    @Query("SELECT * FROM ai_os_config WHERE institutionId = :institutionId ORDER BY rowid DESC LIMIT 1")
    fun observeConfig(institutionId: String): Flow<AIOSConfigEntity?>

    @Query("SELECT * FROM ai_services ORDER BY rowid DESC LIMIT 12")
    fun observeServices(): Flow<List<AIOSServiceEntity>>

    @Query("SELECT * FROM agent_registry ORDER BY rowid DESC LIMIT 12")
    fun observeAgents(): Flow<List<AgentRegistryEntity>>

    @Query("SELECT * FROM workflow_history WHERE userId = :userId ORDER BY rowid DESC LIMIT 1")
    fun observeWorkflow(userId: String): Flow<WorkflowHistoryEntity?>

    @Query("SELECT * FROM memory_core WHERE userId = :userId ORDER BY rowid DESC LIMIT 1")
    fun observeMemory(userId: String): Flow<MemoryCoreEntity?>

    @Query("SELECT * FROM security_logs WHERE userId = :userId ORDER BY createdAt DESC LIMIT 1")
    fun observeSecurity(userId: String): Flow<SecurityLogEntity?>

    @Query("SELECT * FROM system_events ORDER BY createdAt DESC LIMIT 10")
    fun observeEvents(): Flow<List<SystemEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertConfig(config: AIOSConfigEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertServices(services: List<AIOSServiceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAgents(agents: List<AgentRegistryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertWorkflow(workflow: WorkflowHistoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMemory(memory: MemoryCoreEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertResource(resource: ResourceRegistryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertEvent(event: SystemEventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSecurity(log: SecurityLogEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertExtension(extension: ExtensionRegistryEntity)
}

