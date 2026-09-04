package com.rola.app.domain.repository

import com.rola.app.domain.model.ObjectModel
import kotlinx.coroutines.flow.Flow

interface LearningRepository {
    fun observeFeaturedObjects(): Flow<List<ObjectModel>>
    fun searchObjects(query: String): Flow<List<ObjectModel>>
    suspend fun getObjectById(objectId: String): ObjectModel?
    suspend fun getObjectByName(name: String): ObjectModel?
}
