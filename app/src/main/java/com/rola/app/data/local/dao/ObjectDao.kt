package com.rola.app.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.rola.app.data.local.entity.ObjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ObjectDao {
    @Query("SELECT * FROM learning_objects ORDER BY name ASC")
    fun observeObjects(): Flow<List<ObjectEntity>>

    @Query("SELECT * FROM learning_objects WHERE objectId = :objectId LIMIT 1")
    suspend fun getObjectById(objectId: String): ObjectEntity?

    @Query("SELECT * FROM learning_objects WHERE LOWER(name) = LOWER(:name) LIMIT 1")
    suspend fun getObjectByName(name: String): ObjectEntity?
}
