package com.rola.app.di

import android.content.Context
import androidx.room.Room
import com.rola.app.data.database.AIInfrastructureDao
import com.rola.app.data.database.AIOSCoreDao
import com.rola.app.data.database.AIResearchDao
import com.rola.app.data.database.AIMetaverseDao
import com.rola.app.data.database.ASICoreDao
import com.rola.app.data.database.AGIDao
import com.rola.app.data.database.AGINetworkDao
import com.rola.app.data.database.AITeacherDao
import com.rola.app.data.database.AppDatabase
import com.rola.app.data.database.AdaptiveLearningDao
import com.rola.app.data.database.AICivilizationDao
import com.rola.app.data.database.ChatMessageDao
import com.rola.app.data.database.CognitiveAIDao
import com.rola.app.data.database.CreativeAIDao
import com.rola.app.data.database.CollectiveAIDao
import com.rola.app.data.database.DigitalEducationSocietyDao
import com.rola.app.data.database.DigitalCompanionDao
import com.rola.app.data.database.EducationSingularityDao
import com.rola.app.data.database.EducationOrchestrationDao
import com.rola.app.data.database.EmbodiedAIDao
import com.rola.app.data.database.EmotionalAIDao
import com.rola.app.data.database.EnterpriseDao
import com.rola.app.data.database.KnowledgeGraphDao
import com.rola.app.data.database.KnowledgeEngineeringDao
import com.rola.app.data.database.LifelongMemoryDao
import com.rola.app.data.database.MasteryAIDao
import com.rola.app.data.database.NeuralAIDao
import com.rola.app.data.database.ObjectDao
import com.rola.app.data.database.PersonalAgentDao
import com.rola.app.data.database.PlanningAIDao
import com.rola.app.data.database.PredictiveAIDao
import com.rola.app.data.database.QuantumAIDao
import com.rola.app.data.database.QuizDao
import com.rola.app.data.database.ReasoningAIDao
import com.rola.app.data.database.QuizResultDao
import com.rola.app.data.database.ResearchDao
import com.rola.app.data.database.ScanHistoryDao
import com.rola.app.data.database.SelfEvolvingAIDao
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
            AppDatabase.MIGRATION_21_22,
            AppDatabase.MIGRATION_22_23,
            AppDatabase.MIGRATION_23_24,
            AppDatabase.MIGRATION_24_25,
            AppDatabase.MIGRATION_25_26,
            AppDatabase.MIGRATION_26_27,
            AppDatabase.MIGRATION_27_28,
            AppDatabase.MIGRATION_28_29,
            AppDatabase.MIGRATION_29_30,
            AppDatabase.MIGRATION_30_31,
            AppDatabase.MIGRATION_31_32,
            AppDatabase.MIGRATION_32_33,
            AppDatabase.MIGRATION_33_34,
            AppDatabase.MIGRATION_34_35,
            AppDatabase.MIGRATION_35_36,
            AppDatabase.MIGRATION_36_37,
            AppDatabase.MIGRATION_37_38,
            AppDatabase.MIGRATION_38_39,
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

    @Provides
    fun provideAIInfrastructureDao(database: AppDatabase): AIInfrastructureDao =
        database.aiInfrastructureDao()

    @Provides
    fun provideAIOSCoreDao(database: AppDatabase): AIOSCoreDao = database.aiOSCoreDao()

    @Provides
    fun provideDigitalCompanionDao(database: AppDatabase): DigitalCompanionDao =
        database.digitalCompanionDao()

    @Provides
    fun provideCollectiveAIDao(database: AppDatabase): CollectiveAIDao =
        database.collectiveAIDao()

    @Provides
    fun provideEducationSingularityDao(database: AppDatabase): EducationSingularityDao =
        database.educationSingularityDao()

    @Provides
    fun provideAICivilizationDao(database: AppDatabase): AICivilizationDao =
        database.aiCivilizationDao()

    @Provides
    fun provideLifelongMemoryDao(database: AppDatabase): LifelongMemoryDao =
        database.lifelongMemoryDao()

    @Provides
    fun providePredictiveAIDao(database: AppDatabase): PredictiveAIDao =
        database.predictiveAIDao()

    @Provides
    fun provideEmotionalAIDao(database: AppDatabase): EmotionalAIDao =
        database.emotionalAIDao()

    @Provides
    fun provideCreativeAIDao(database: AppDatabase): CreativeAIDao =
        database.creativeAIDao()

    @Provides
    fun provideAIResearchDao(database: AppDatabase): AIResearchDao =
        database.aiResearchDao()

    @Provides
    fun provideKnowledgeEngineeringDao(database: AppDatabase): KnowledgeEngineeringDao =
        database.knowledgeEngineeringDao()

    @Provides
    fun provideReasoningAIDao(database: AppDatabase): ReasoningAIDao =
        database.reasoningAIDao()

    @Provides
    fun providePlanningAIDao(database: AppDatabase): PlanningAIDao =
        database.planningAIDao()

    @Provides
    fun provideMasteryAIDao(database: AppDatabase): MasteryAIDao =
        database.masteryAIDao()

    @Provides
    fun providePersonalAgentDao(database: AppDatabase): PersonalAgentDao =
        database.personalAgentDao()

    @Provides
    fun provideEducationOrchestrationDao(database: AppDatabase): EducationOrchestrationDao =
        database.educationOrchestrationDao()

    @Provides
    fun provideSelfEvolvingAIDao(database: AppDatabase): SelfEvolvingAIDao =
        database.selfEvolvingAIDao()
}
