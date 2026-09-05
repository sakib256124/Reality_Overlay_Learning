package com.rola.app.di

import android.content.Context
import androidx.room.Room
import com.rola.app.data.database.AIMetaverseDao
import com.rola.app.data.database.ASICoreDao
import com.rola.app.data.database.AGIDao
import com.rola.app.data.database.AGINetworkDao
import com.rola.app.data.database.AITeacherDao
import com.rola.app.data.database.AppDatabase
import com.rola.app.data.database.AdaptiveLearningDao
import com.rola.app.data.database.ChatMessageDao
import com.rola.app.data.database.CognitiveAIDao
import com.rola.app.data.database.DigitalEducationSocietyDao
import com.rola.app.data.database.EmbodiedAIDao
import com.rola.app.data.database.EnterpriseDao
import com.rola.app.data.database.KnowledgeGraphDao
import com.rola.app.data.database.NeuralAIDao
import com.rola.app.data.database.ObjectDao
import com.rola.app.data.database.QuantumAIDao
import com.rola.app.data.database.QuizDao
import com.rola.app.data.database.QuizResultDao
import com.rola.app.data.database.ResearchDao
import com.rola.app.data.database.ScanHistoryDao
import com.rola.app.data.database.SpatialAIDao
import com.rola.app.data.database.TranslationDao
import com.rola.app.data.database.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "rola.db",
    )
        .addMigrations(
            AppDatabase.MIGRATION_1_2,
            AppDatabase.MIGRATION_2_3,
            AppDatabase.MIGRATION_3_4,
            AppDatabase.MIGRATION_4_5,
            AppDatabase.MIGRATION_5_6,
            AppDatabase.MIGRATION_6_7,
            AppDatabase.MIGRATION_7_8,
            AppDatabase.MIGRATION_8_9,
            AppDatabase.MIGRATION_9_10,
            AppDatabase.MIGRATION_10_11,
            AppDatabase.MIGRATION_11_12,
            AppDatabase.MIGRATION_12_13,
            AppDatabase.MIGRATION_13_14,
            AppDatabase.MIGRATION_14_15,
            AppDatabase.MIGRATION_15_16,
            AppDatabase.MIGRATION_16_17,
            AppDatabase.MIGRATION_17_18,
            AppDatabase.MIGRATION_18_19,
            AppDatabase.MIGRATION_19_20,
            AppDatabase.MIGRATION_20_21,
        )
        .build()

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao()

    @Provides
    fun provideObjectDao(database: AppDatabase): ObjectDao = database.objectDao()

    @Provides
    fun provideScanHistoryDao(database: AppDatabase): ScanHistoryDao = database.scanHistoryDao()

    @Provides
    fun provideQuizDao(database: AppDatabase): QuizDao = database.quizDao()

    @Provides
    fun provideQuizResultDao(database: AppDatabase): QuizResultDao = database.quizResultDao()

    @Provides
    fun provideChatMessageDao(database: AppDatabase): ChatMessageDao = database.chatMessageDao()

    @Provides
    fun provideTranslationDao(database: AppDatabase): TranslationDao = database.translationDao()

    @Provides
    fun provideAdaptiveLearningDao(database: AppDatabase): AdaptiveLearningDao = database.adaptiveLearningDao()

    @Provides
    fun provideKnowledgeGraphDao(database: AppDatabase): KnowledgeGraphDao = database.knowledgeGraphDao()

    @Provides
    fun provideResearchDao(database: AppDatabase): ResearchDao = database.researchDao()

    @Provides
    fun provideEnterpriseDao(database: AppDatabase): EnterpriseDao = database.enterpriseDao()

    @Provides
    fun provideAITeacherDao(database: AppDatabase): AITeacherDao = database.aiTeacherDao()

    @Provides
    fun provideAGIDao(database: AppDatabase): AGIDao = database.agiDao()

    @Provides
    fun provideSpatialAIDao(database: AppDatabase): SpatialAIDao = database.spatialAIDao()

    @Provides
    fun provideCognitiveAIDao(database: AppDatabase): CognitiveAIDao = database.cognitiveAIDao()

    @Provides
    fun provideEmbodiedAIDao(database: AppDatabase): EmbodiedAIDao = database.embodiedAIDao()

    @Provides
    fun provideNeuralAIDao(database: AppDatabase): NeuralAIDao = database.neuralAIDao()

    @Provides
    fun provideAGINetworkDao(database: AppDatabase): AGINetworkDao = database.agiNetworkDao()

    @Provides
    fun provideQuantumAIDao(database: AppDatabase): QuantumAIDao = database.quantumAIDao()

    @Provides
    fun provideASICoreDao(database: AppDatabase): ASICoreDao = database.asiCoreDao()

    @Provides
    fun provideDigitalEducationSocietyDao(database: AppDatabase): DigitalEducationSocietyDao =
        database.digitalEducationSocietyDao()

    @Provides
    fun provideAIMetaverseDao(database: AppDatabase): AIMetaverseDao = database.aiMetaverseDao()
}
