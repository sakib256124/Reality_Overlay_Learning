package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.ObjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ObjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertObject(objectEntity: ObjectEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertObjects(objects: List<ObjectEntity>)

    @Query("SELECT * FROM learning_objects ORDER BY name ASC")
    fun observeObjects(): Flow<List<ObjectEntity>>

    @Query("SELECT * FROM learning_objects WHERE objectId = :objectId LIMIT 1")
    suspend fun getObjectById(objectId: String): ObjectEntity?

    @Query("SELECT * FROM learning_objects WHERE LOWER(name) = LOWER(:name) LIMIT 1")
    suspend fun getObjectByName(name: String): ObjectEntity?

    @Query(
        """
        SELECT * FROM learning_objects
        WHERE name LIKE '%' || :query || '%'
            OR category LIKE '%' || :query || '%'
            OR scientificName LIKE '%' || :query || '%'
        ORDER BY name ASC
        """,
    )
    fun searchObjects(query: String): Flow<List<ObjectEntity>>

    @Query("SELECT * FROM learning_objects WHERE isSynced = 0")
    suspend fun getUnsyncedObjects(): List<ObjectEntity>

    @Query("UPDATE learning_objects SET isSynced = 1 WHERE objectId = :objectId")
    suspend fun markObjectSynced(objectId: String)
}
