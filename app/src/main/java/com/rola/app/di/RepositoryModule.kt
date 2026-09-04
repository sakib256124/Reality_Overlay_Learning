package com.rola.app.di

import com.rola.app.data.repository.LearningHistoryRepositoryImpl
import com.rola.app.data.repository.LearningRepositoryImpl
import com.rola.app.domain.repository.LearningHistoryRepository
import com.rola.app.domain.repository.LearningRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindLearningRepository(
        implementation: LearningRepositoryImpl,
    ): LearningRepository

    @Binds
    @Singleton
    abstract fun bindLearningHistoryRepository(
        implementation: LearningHistoryRepositoryImpl,
    ): LearningHistoryRepository
}
