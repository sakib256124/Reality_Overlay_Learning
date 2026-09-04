package com.rola.app.data.repository

import com.rola.app.data.database.ObjectDao
import com.rola.app.domain.model.ObjectModel
import com.rola.app.domain.repository.LearningRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LearningRepositoryImpl @Inject constructor(
    private val objectDao: ObjectDao,
) : LearningRepository {
    override fun observeFeaturedObjects(): Flow<List<ObjectModel>> =
        objectDao.observeObjects().map { entities -> entities.map { it.toObjectModel() } }

    override fun searchObjects(query: String): Flow<List<ObjectModel>> =
        objectDao.searchObjects(query).map { entities -> entities.map { it.toObjectModel() } }

    override suspend fun getObjectById(objectId: String): ObjectModel? =
        objectDao.getObjectById(objectId)?.toObjectModel()

    override suspend fun getObjectByName(name: String): ObjectModel? =
        objectDao.getObjectByName(name)?.toObjectModel()
}
