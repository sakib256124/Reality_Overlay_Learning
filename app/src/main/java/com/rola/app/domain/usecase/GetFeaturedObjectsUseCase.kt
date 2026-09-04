package com.rola.app.domain.usecase

import com.rola.app.domain.model.ObjectModel
import com.rola.app.domain.repository.LearningRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFeaturedObjectsUseCase @Inject constructor(
    private val repository: LearningRepository,
) {
    operator fun invoke(): Flow<List<ObjectModel>> = repository.observeFeaturedObjects()
}
