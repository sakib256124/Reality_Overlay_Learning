package com.rola.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rola.app.data.database.converters.QuizConverters
import com.rola.app.data.database.converters.StringListConverter
import com.rola.app.data.database.entities.AICreativeOutputEntity
import com.rola.app.data.database.entities.MetaverseAnalyticsEntity
import com.rola.app.data.database.entities.MetaverseAvatarInteractionEntity
import com.rola.app.data.database.entities.MetaverseCommunitySpaceEntity
import com.rola.app.data.database.entities.MetaverseDigitalSpaceEntity
import com.rola.app.data.database.entities.MetaverseLearningAvatarEntity
import com.rola.app.data.database.entities.MetaverseSessionEntity
import com.rola.app.data.database.entities.MetaverseVirtualClassroomEntity
import com.rola.app.data.database.entities.MetaverseVirtualExperimentEntity
import com.rola.app.data.database.entities.MetaverseVirtualWorldEntity
import com.rola.app.data.database.entities.AIEvolutionHistoryEntity
import com.rola.app.data.database.entities.AGIMemoryEntity
import com.rola.app.data.database.entities.AGINetworkAgentCommunicationEntity
import com.rola.app.data.database.entities.AGINetworkAgentEntity
import com.rola.app.data.database.entities.AGINetworkAgentTaskEntity
import com.rola.app.data.database.entities.AGINetworkAnalyticsEntity
import com.rola.app.data.database.entities.AGINetworkCurriculumEvolutionEntity
import com.rola.app.data.database.entities.AGINetworkDecisionEntity
import com.rola.app.data.database.entities.AGINetworkGovernanceRecordEntity
import com.rola.app.data.database.entities.AGINetworkKnowledgeEvolutionEntity
import com.rola.app.data.database.entities.AIDecisionEntity
import com.rola.app.data.database.entities.ASIModelEntity
import com.rola.app.data.database.entities.ASIProfileEntity
import com.rola.app.data.database.entities.ASIGovernanceRecordEntity
import com.rola.app.data.database.entities.ChatMessageEntity
import com.rola.app.data.database.entities.AssignmentEntity
import com.rola.app.data.database.entities.AssignmentSubmissionEntity
import com.rola.app.data.database.entities.BehaviorAnalyticsEntity
import com.rola.app.data.database.entities.ClassGroupEntity
import com.rola.app.data.database.entities.ClassroomSessionEntity
import com.rola.app.data.database.entities.CognitiveAIDecisionEntity
import com.rola.app.data.database.entities.CognitiveActivityEntity
import com.rola.app.data.database.entities.CognitiveLearnerModelEntity
import com.rola.app.data.database.entities.CognitiveMemoryRecordEntity
import com.rola.app.data.database.entities.CognitiveProfileEntity
import com.rola.app.data.database.entities.CognitiveReportEntity
import com.rola.app.data.database.entities.CognitiveSkillMapEntity
import com.rola.app.data.database.entities.BrainSignalEntity
import com.rola.app.data.database.entities.GlobalEducationInsightEntity
import com.rola.app.data.database.entities.RobotAnalyticsEntity
import com.rola.app.data.database.entities.RobotClassroomSessionEntity
import com.rola.app.data.database.entities.RobotInteractionEntity
import com.rola.app.data.database.entities.RobotMemoryEntity
import com.rola.app.data.database.entities.RobotProfileEntity
import com.rola.app.data.database.entities.RobotSessionEntity
import com.rola.app.data.database.entities.RobotTeachingActivityEntity
import com.rola.app.data.database.entities.StudentRobotHistoryEntity
import com.rola.app.data.database.entities.AICurriculumEntity
import com.rola.app.data.database.entities.AITeacherAssessmentEntity
import com.rola.app.data.database.entities.ARLearningActivityEntity
import com.rola.app.data.database.entities.AdaptiveTeachingPlanEntity
import com.rola.app.data.database.entities.KnowledgeNodeEntity
import com.rola.app.data.database.entities.KnowledgeRelationEntity
import com.rola.app.data.database.entities.LanguageEntity
import com.rola.app.data.database.entities.LearningProfileEntity
import com.rola.app.data.database.entities.LearningPathEntity
import com.rola.app.data.database.entities.ObjectEntity
import com.rola.app.data.database.entities.QuizEntity
import com.rola.app.data.database.entities.QuizResultEntity
import com.rola.app.data.database.entities.RecommendationEntity
import com.rola.app.data.database.entities.ContentVersionEntity
import com.rola.app.data.database.entities.CourseEntity
import com.rola.app.data.database.entities.CurriculumModuleEntity
import com.rola.app.data.database.entities.DepartmentEntity
import com.rola.app.data.database.entities.EnterpriseAuditLogEntity
import com.rola.app.data.database.entities.EnterpriseRoleEntity
import com.rola.app.data.database.entities.EmotionAnalyticsEntity
import com.rola.app.data.database.entities.FutureRecommendationEntity
import com.rola.app.data.database.entities.GeneratedLessonEntity
import com.rola.app.data.database.entities.InstitutionAnalyticsEntity
import com.rola.app.data.database.entities.InstitutionEntity
import com.rola.app.data.database.entities.InstitutionMemberEntity
import com.rola.app.data.database.entities.KnowledgeEvolutionEntity
import com.rola.app.data.database.entities.KnowledgeEvolutionRecordEntity
import com.rola.app.data.database.entities.KnowledgeUpdateEntity
import com.rola.app.data.database.entities.LMSConnectionEntity
import com.rola.app.data.database.entities.LearnerModelEntity
import com.rola.app.data.database.entities.LearningGoalEntity
import com.rola.app.data.database.entities.HumanAIInteractionEntity
import com.rola.app.data.database.entities.LearningPatternEntity
import com.rola.app.data.database.entities.LearningMaterialEntity
import com.rola.app.data.database.entities.LearningEnvironmentEntity
import com.rola.app.data.database.entities.LearningPredictionEntity
import com.rola.app.data.database.entities.AttentionRecordEntity
import com.rola.app.data.database.entities.LessonAnalyticsEntity
import com.rola.app.data.database.entities.NeuralCognitiveStateEntity
import com.rola.app.data.database.entities.NeuralInteractionEntity
import com.rola.app.data.database.entities.NeuralLearningPredictionEntity
import com.rola.app.data.database.entities.NeuralLearningStateEntity
import com.rola.app.data.database.entities.NeuralProfileEntity
import com.rola.app.data.database.entities.KnowledgeDiscoveryRecordEntity
import com.rola.app.data.database.entities.LearningOptimizationHistoryEntity
import com.rola.app.data.database.entities.OptimizationResultEntity
import com.rola.app.data.database.entities.QuantumAnalyticsEntity
import com.rola.app.data.database.entities.QuantumDecisionEntity
import com.rola.app.data.database.entities.QuantumModelEntity
import com.rola.app.data.database.entities.QuantumPredictionEntity
import com.rola.app.data.database.entities.QuantumProfileEntity
import com.rola.app.data.database.entities.ReasoningHistoryEntity
import com.rola.app.data.database.entities.ResearchTaskEntity
import com.rola.app.data.database.entities.ScanHistoryEntity
import com.rola.app.data.database.entities.ScientificSourceEntity
import com.rola.app.data.database.entities.SkillGraphEntity
import com.rola.app.data.database.entities.DigitalTwinEntity
import com.rola.app.data.database.entities.DigitalAvatarEntity
import com.rola.app.data.database.entities.EducationInstitutionEntity
import com.rola.app.data.database.entities.GlobalEducationNetworkEntity
import com.rola.app.data.database.entities.GlobalLearningAnalyticsEntity
import com.rola.app.data.database.entities.GovernancePolicyEntity
import com.rola.app.data.database.entities.KnowledgeCommunityEntity
import com.rola.app.data.database.entities.KnowledgeExchangeHistoryEntity
import com.rola.app.data.database.entities.Spatial3DAssetEntity
import com.rola.app.data.database.entities.SpatialInteractionHistoryEntity
import com.rola.app.data.database.entities.SpatialSessionEntity
import com.rola.app.data.database.entities.SpatialSimulationEntity
import com.rola.app.data.database.entities.SpatialWorldEntity
import com.rola.app.data.database.entities.StudentReportEntity
import com.rola.app.data.database.entities.SelfImprovementLogEntity
import com.rola.app.data.database.entities.SocietyAIAgentEntity
import com.rola.app.data.database.entities.SocietyInnovationRecordEntity
import com.rola.app.data.database.entities.TeacherReviewEntity
import com.rola.app.data.database.entities.TranslationCacheEntity
import com.rola.app.data.database.entities.UserEntity
import com.rola.app.data.database.entities.VirtualClassroomEntity
import com.rola.app.data.database.entities.VirtualLessonEntity
import com.rola.app.data.database.entities.PersonalLearningPlanEntity

@Database(
    entities = [
        UserEntity::class,
        ObjectEntity::class,
        ScanHistoryEntity::class,
        QuizEntity::class,
        QuizResultEntity::class,
        ChatMessageEntity::class,
        LanguageEntity::class,
        TranslationCacheEntity::class,
        LearningProfileEntity::class,
        RecommendationEntity::class,
        KnowledgeNodeEntity::class,
        KnowledgeRelationEntity::class,
        LearningPathEntity::class,
        ResearchTaskEntity::class,
        ScientificSourceEntity::class,
        KnowledgeUpdateEntity::class,
        LearningMaterialEntity::class,
        ContentVersionEntity::class,
        EnterpriseRoleEntity::class,
        InstitutionEntity::class,
        DepartmentEntity::class,
        CourseEntity::class,
        ClassGroupEntity::class,
        InstitutionMemberEntity::class,
        AssignmentEntity::class,
        AssignmentSubmissionEntity::class,
        ClassroomSessionEntity::class,
        InstitutionAnalyticsEntity::class,
        StudentReportEntity::class,
        LMSConnectionEntity::class,
        EnterpriseAuditLogEntity::class,
        AICurriculumEntity::class,
        CurriculumModuleEntity::class,
        GeneratedLessonEntity::class,
        ARLearningActivityEntity::class,
        AITeacherAssessmentEntity::class,
        AdaptiveTeachingPlanEntity::class,
        TeacherReviewEntity::class,
        LessonAnalyticsEntity::class,
        AGIMemoryEntity::class,
        LearnerModelEntity::class,
        SkillGraphEntity::class,
        AIDecisionEntity::class,
        LearningGoalEntity::class,
        FutureRecommendationEntity::class,
        KnowledgeEvolutionEntity::class,
        SpatialWorldEntity::class,
        DigitalTwinEntity::class,
        VirtualClassroomEntity::class,
        VirtualLessonEntity::class,
        SpatialSimulationEntity::class,
        SpatialSessionEntity::class,
        SpatialInteractionHistoryEntity::class,
        LearningEnvironmentEntity::class,
        Spatial3DAssetEntity::class,
        CognitiveProfileEntity::class,
        CognitiveLearnerModelEntity::class,
        CognitiveMemoryRecordEntity::class,
        LearningPatternEntity::class,
        BehaviorAnalyticsEntity::class,
        EmotionAnalyticsEntity::class,
        CognitiveSkillMapEntity::class,
        LearningPredictionEntity::class,
        PersonalLearningPlanEntity::class,
        CognitiveAIDecisionEntity::class,
        CognitiveActivityEntity::class,
        RobotProfileEntity::class,
        RobotSessionEntity::class,
        RobotInteractionEntity::class,
        RobotMemoryEntity::class,
        RobotTeachingActivityEntity::class,
        RobotClassroomSessionEntity::class,
        RobotAnalyticsEntity::class,
        StudentRobotHistoryEntity::class,
        NeuralProfileEntity::class,
        BrainSignalEntity::class,
        NeuralCognitiveStateEntity::class,
        NeuralLearningStateEntity::class,
        NeuralInteractionEntity::class,
        AttentionRecordEntity::class,
        NeuralLearningPredictionEntity::class,
        CognitiveReportEntity::class,
        AGINetworkAgentEntity::class,
        AGINetworkAgentTaskEntity::class,
        AGINetworkAgentCommunicationEntity::class,
        AIEvolutionHistoryEntity::class,
        AGINetworkKnowledgeEvolutionEntity::class,
        AGINetworkDecisionEntity::class,
        AGINetworkCurriculumEvolutionEntity::class,
        AGINetworkAnalyticsEntity::class,
        AGINetworkGovernanceRecordEntity::class,
        QuantumProfileEntity::class,
        QuantumModelEntity::class,
        OptimizationResultEntity::class,
        QuantumDecisionEntity::class,
        LearningOptimizationHistoryEntity::class,
        QuantumPredictionEntity::class,
        KnowledgeDiscoveryRecordEntity::class,
        QuantumAnalyticsEntity::class,
        ASIProfileEntity::class,
        ASIModelEntity::class,
        ReasoningHistoryEntity::class,
        KnowledgeEvolutionRecordEntity::class,
        SelfImprovementLogEntity::class,
        AICreativeOutputEntity::class,
        HumanAIInteractionEntity::class,
        ASIGovernanceRecordEntity::class,
        GlobalEducationInsightEntity::class,
        GlobalEducationNetworkEntity::class,
        KnowledgeCommunityEntity::class,
        SocietyAIAgentEntity::class,
        EducationInstitutionEntity::class,
        SocietyInnovationRecordEntity::class,
        GlobalLearningAnalyticsEntity::class,
        DigitalAvatarEntity::class,
        GovernancePolicyEntity::class,
        KnowledgeExchangeHistoryEntity::class,
        MetaverseVirtualWorldEntity::class,
        MetaverseDigitalSpaceEntity::class,
        MetaverseLearningAvatarEntity::class,
        MetaverseVirtualClassroomEntity::class,
        MetaverseSessionEntity::class,
        MetaverseAvatarInteractionEntity::class,
        MetaverseVirtualExperimentEntity::class,
        MetaverseCommunitySpaceEntity::class,
        MetaverseAnalyticsEntity::class,
    ],
    version = 21,
    exportSchema = true,
)
@TypeConverters(StringListConverter::class, QuizConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun objectDao(): ObjectDao
    abstract fun scanHistoryDao(): ScanHistoryDao
    abstract fun quizDao(): QuizDao
    abstract fun quizResultDao(): QuizResultDao
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun translationDao(): TranslationDao
    abstract fun adaptiveLearningDao(): AdaptiveLearningDao
    abstract fun knowledgeGraphDao(): KnowledgeGraphDao
    abstract fun researchDao(): ResearchDao
    abstract fun enterpriseDao(): EnterpriseDao
    abstract fun aiTeacherDao(): AITeacherDao
    abstract fun agiDao(): AGIDao
    abstract fun spatialAIDao(): SpatialAIDao
    abstract fun cognitiveAIDao(): CognitiveAIDao
    abstract fun embodiedAIDao(): EmbodiedAIDao
    abstract fun neuralAIDao(): NeuralAIDao
    abstract fun agiNetworkDao(): AGINetworkDao
    abstract fun quantumAIDao(): QuantumAIDao
    abstract fun asiCoreDao(): ASICoreDao
    abstract fun digitalEducationSocietyDao(): DigitalEducationSocietyDao
    abstract fun aiMetaverseDao(): AIMetaverseDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE learning_objects ADD COLUMN facts TEXT NOT NULL DEFAULT ''")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE learning_objects ADD COLUMN isSynced INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE learning_objects ADD COLUMN updatedAt INTEGER NOT NULL DEFAULT 0")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS users (
                        userId TEXT NOT NULL PRIMARY KEY,
                        name TEXT NOT NULL,
                        email TEXT NOT NULL,
                        profileImage TEXT NOT NULL,
                        isSynced INTEGER NOT NULL DEFAULT 0,
                        updatedAt INTEGER NOT NULL DEFAULT 0
                    )
                    """.trimIndent(),
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS scan_history (
                        scanId TEXT NOT NULL PRIMARY KEY,
                        userId TEXT NOT NULL,
                        objectId TEXT NOT NULL,
                        timestamp INTEGER NOT NULL,
                        confidenceScore REAL NOT NULL,
                        learningStatus TEXT NOT NULL,
                        isSynced INTEGER NOT NULL DEFAULT 0,
                        updatedAt INTEGER NOT NULL DEFAULT 0,
                        FOREIGN KEY(userId) REFERENCES users(userId) ON UPDATE NO ACTION ON DELETE CASCADE,
                        FOREIGN KEY(objectId) REFERENCES learning_objects(objectId) ON UPDATE NO ACTION ON DELETE CASCADE
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_scan_history_userId ON scan_history(userId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_scan_history_objectId ON scan_history(objectId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_scan_history_timestamp ON scan_history(timestamp)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_scan_history_isSynced ON scan_history(isSynced)")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS quizzes (
                        quizId TEXT NOT NULL PRIMARY KEY,
                        objectId TEXT NOT NULL,
                        title TEXT NOT NULL,
                        difficulty TEXT NOT NULL,
                        questionsJson TEXT NOT NULL,
                        createdDate INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_quizzes_objectId ON quizzes(objectId)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS quiz_results (
                        resultId TEXT NOT NULL PRIMARY KEY,
                        userId TEXT NOT NULL,
                        quizId TEXT NOT NULL,
                        score INTEGER NOT NULL,
                        totalQuestions INTEGER NOT NULL,
                        percentage INTEGER NOT NULL,
                        completionTime INTEGER NOT NULL,
                        timestamp INTEGER NOT NULL,
                        isSynced INTEGER NOT NULL DEFAULT 0,
                        updatedAt INTEGER NOT NULL DEFAULT 0,
                        FOREIGN KEY(userId) REFERENCES users(userId) ON UPDATE NO ACTION ON DELETE CASCADE
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_quiz_results_userId ON quiz_results(userId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_quiz_results_quizId ON quiz_results(quizId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_quiz_results_timestamp ON quiz_results(timestamp)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_quiz_results_isSynced ON quiz_results(isSynced)")
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS chat_messages (
                        messageId TEXT NOT NULL PRIMARY KEY,
                        userId TEXT NOT NULL,
                        objectId TEXT,
                        role TEXT NOT NULL,
                        content TEXT NOT NULL,
                        timestamp INTEGER NOT NULL,
                        isGrounded INTEGER NOT NULL DEFAULT 1
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_chat_messages_userId ON chat_messages(userId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_chat_messages_objectId ON chat_messages(objectId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_chat_messages_timestamp ON chat_messages(timestamp)")
            }
        }

        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS languages (
                        languageCode TEXT NOT NULL PRIMARY KEY,
                        languageName TEXT NOT NULL,
                        nativeName TEXT NOT NULL,
                        supportedVoice INTEGER NOT NULL,
                        isDownloaded INTEGER NOT NULL DEFAULT 0,
                        lastUsedAt INTEGER NOT NULL DEFAULT 0
                    )
                    """.trimIndent(),
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS translation_cache (
                        cacheId TEXT NOT NULL PRIMARY KEY,
                        sourceText TEXT NOT NULL,
                        sourceLanguage TEXT NOT NULL,
                        targetLanguage TEXT NOT NULL,
                        translatedText TEXT NOT NULL,
                        timestamp INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL(
                    "CREATE INDEX IF NOT EXISTS index_translation_cache_sourceLanguage_targetLanguage ON translation_cache(sourceLanguage, targetLanguage)",
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_translation_cache_timestamp ON translation_cache(timestamp)")
            }
        }

        val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS learning_profiles (
                        userId TEXT NOT NULL PRIMARY KEY,
                        totalObjectsLearned INTEGER NOT NULL,
                        averageQuizScore INTEGER NOT NULL,
                        favoriteCategories TEXT NOT NULL,
                        weakAreas TEXT NOT NULL,
                        learningLevel TEXT NOT NULL,
                        learningStreak INTEGER NOT NULL,
                        totalLearningTimeMillis INTEGER NOT NULL,
                        frequentlySearchedTopics TEXT NOT NULL,
                        difficultConcepts TEXT NOT NULL,
                        preferredLanguage TEXT NOT NULL,
                        learningSpeed TEXT NOT NULL,
                        personalizationEnabled INTEGER NOT NULL DEFAULT 1,
                        isSynced INTEGER NOT NULL DEFAULT 0,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS recommendations (
                        recommendationId TEXT NOT NULL PRIMARY KEY,
                        userId TEXT NOT NULL,
                        title TEXT NOT NULL,
                        description TEXT NOT NULL,
                        topic TEXT NOT NULL,
                        type TEXT NOT NULL,
                        priority TEXT NOT NULL,
                        targetSkillLevel TEXT NOT NULL,
                        createdAt INTEGER NOT NULL,
                        completed INTEGER NOT NULL DEFAULT 0,
                        isSynced INTEGER NOT NULL DEFAULT 0,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_recommendations_userId ON recommendations(userId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_recommendations_priority ON recommendations(priority)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_recommendations_completed ON recommendations(completed)")
            }
        }

        val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS knowledge_nodes (
                        nodeId TEXT NOT NULL PRIMARY KEY,
                        name TEXT NOT NULL,
                        type TEXT NOT NULL,
                        description TEXT NOT NULL,
                        category TEXT NOT NULL,
                        aliases TEXT NOT NULL DEFAULT '',
                        tags TEXT NOT NULL DEFAULT '',
                        verified INTEGER NOT NULL DEFAULT 1,
                        source TEXT NOT NULL DEFAULT 'ROLA Knowledge Graph',
                        isSynced INTEGER NOT NULL DEFAULT 1,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_knowledge_nodes_name ON knowledge_nodes(name)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_knowledge_nodes_type ON knowledge_nodes(type)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_knowledge_nodes_category ON knowledge_nodes(category)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_knowledge_nodes_verified ON knowledge_nodes(verified)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS knowledge_relations (
                        relationId TEXT NOT NULL PRIMARY KEY,
                        sourceNodeId TEXT NOT NULL,
                        targetNodeId TEXT NOT NULL,
                        type TEXT NOT NULL,
                        description TEXT NOT NULL,
                        confidence REAL NOT NULL DEFAULT 1.0,
                        verified INTEGER NOT NULL DEFAULT 1,
                        createdBy TEXT NOT NULL DEFAULT 'system',
                        isSynced INTEGER NOT NULL DEFAULT 1,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_knowledge_relations_sourceNodeId ON knowledge_relations(sourceNodeId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_knowledge_relations_targetNodeId ON knowledge_relations(targetNodeId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_knowledge_relations_type ON knowledge_relations(type)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_knowledge_relations_verified ON knowledge_relations(verified)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS learning_paths (
                        pathId TEXT NOT NULL PRIMARY KEY,
                        title TEXT NOT NULL,
                        description TEXT NOT NULL,
                        targetLevel TEXT NOT NULL,
                        topic TEXT NOT NULL,
                        nodeIds TEXT NOT NULL DEFAULT '',
                        estimatedMinutes INTEGER NOT NULL,
                        generatedBy TEXT NOT NULL DEFAULT 'knowledge_graph',
                        isSynced INTEGER NOT NULL DEFAULT 1,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_learning_paths_topic ON learning_paths(topic)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_learning_paths_targetLevel ON learning_paths(targetLevel)")
            }
        }

        val MIGRATION_8_9 = object : Migration(8, 9) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS research_tasks (
                        taskId TEXT NOT NULL PRIMARY KEY,
                        topic TEXT NOT NULL,
                        reason TEXT NOT NULL,
                        priority TEXT NOT NULL,
                        status TEXT NOT NULL,
                        suggestedSources TEXT NOT NULL DEFAULT '',
                        assignedTo TEXT NOT NULL,
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_research_tasks_topic ON research_tasks(topic)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_research_tasks_status ON research_tasks(status)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_research_tasks_priority ON research_tasks(priority)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS scientific_sources (
                        sourceId TEXT NOT NULL PRIMARY KEY,
                        title TEXT NOT NULL,
                        url TEXT NOT NULL,
                        sourceType TEXT NOT NULL,
                        reliability TEXT NOT NULL,
                        publisher TEXT NOT NULL,
                        authors TEXT NOT NULL DEFAULT '',
                        publicationYear INTEGER,
                        topics TEXT NOT NULL DEFAULT '',
                        trusted INTEGER NOT NULL DEFAULT 0,
                        addedAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_scientific_sources_sourceType ON scientific_sources(sourceType)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_scientific_sources_reliability ON scientific_sources(reliability)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_scientific_sources_trusted ON scientific_sources(trusted)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS knowledge_updates (
                        updateId TEXT NOT NULL PRIMARY KEY,
                        taskId TEXT NOT NULL,
                        topic TEXT NOT NULL,
                        summary TEXT NOT NULL,
                        definitions TEXT NOT NULL DEFAULT '',
                        properties TEXT NOT NULL DEFAULT '',
                        applications TEXT NOT NULL DEFAULT '',
                        examples TEXT NOT NULL DEFAULT '',
                        relationSuggestions TEXT NOT NULL DEFAULT '',
                        difficultyLevel TEXT NOT NULL,
                        sourceIds TEXT NOT NULL DEFAULT '',
                        reliabilityScore REAL NOT NULL,
                        duplicateRisk REAL NOT NULL,
                        consistencyScore REAL NOT NULL,
                        contradictionRisk REAL NOT NULL,
                        moderationStatus TEXT NOT NULL,
                        verificationNotes TEXT NOT NULL DEFAULT '',
                        status TEXT NOT NULL,
                        version INTEGER NOT NULL,
                        createdAt INTEGER NOT NULL,
                        approvedAt INTEGER,
                        approvedBy TEXT
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_knowledge_updates_taskId ON knowledge_updates(taskId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_knowledge_updates_topic ON knowledge_updates(topic)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_knowledge_updates_status ON knowledge_updates(status)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_knowledge_updates_createdAt ON knowledge_updates(createdAt)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS learning_materials (
                        materialId TEXT NOT NULL PRIMARY KEY,
                        topic TEXT NOT NULL,
                        materialType TEXT NOT NULL,
                        title TEXT NOT NULL,
                        beginnerExplanation TEXT NOT NULL,
                        advancedExplanation TEXT NOT NULL,
                        summary TEXT NOT NULL,
                        quizQuestions TEXT NOT NULL DEFAULT '',
                        flashcards TEXT NOT NULL DEFAULT '',
                        sourceUpdateId TEXT NOT NULL,
                        version INTEGER NOT NULL,
                        status TEXT NOT NULL,
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_learning_materials_topic ON learning_materials(topic)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_learning_materials_materialType ON learning_materials(materialType)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_learning_materials_status ON learning_materials(status)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS content_versions (
                        versionId TEXT NOT NULL PRIMARY KEY,
                        contentId TEXT NOT NULL,
                        contentType TEXT NOT NULL,
                        version INTEGER NOT NULL,
                        sourceIds TEXT NOT NULL DEFAULT '',
                        changeSummary TEXT NOT NULL,
                        modifiedBy TEXT NOT NULL,
                        createdAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_content_versions_contentId ON content_versions(contentId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_content_versions_contentType ON content_versions(contentType)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_content_versions_createdAt ON content_versions(createdAt)")
            }
        }

        val MIGRATION_9_10 = object : Migration(9, 10) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS enterprise_roles (
                        roleId TEXT NOT NULL PRIMARY KEY,
                        userId TEXT NOT NULL,
                        institutionId TEXT NOT NULL,
                        role TEXT NOT NULL,
                        permissions TEXT NOT NULL DEFAULT '',
                        active INTEGER NOT NULL DEFAULT 1,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_enterprise_roles_institutionId ON enterprise_roles(institutionId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_enterprise_roles_role ON enterprise_roles(role)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS institutions (
                        institutionId TEXT NOT NULL PRIMARY KEY,
                        name TEXT NOT NULL,
                        type TEXT NOT NULL,
                        domain TEXT NOT NULL,
                        region TEXT NOT NULL,
                        adminUserIds TEXT NOT NULL DEFAULT '',
                        active INTEGER NOT NULL DEFAULT 1,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_institutions_name ON institutions(name)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_institutions_active ON institutions(active)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS departments (
                        departmentId TEXT NOT NULL PRIMARY KEY,
                        institutionId TEXT NOT NULL,
                        name TEXT NOT NULL,
                        description TEXT NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_departments_institutionId ON departments(institutionId)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS courses (
                        courseId TEXT NOT NULL PRIMARY KEY,
                        institutionId TEXT NOT NULL,
                        departmentId TEXT NOT NULL,
                        title TEXT NOT NULL,
                        description TEXT NOT NULL,
                        subject TEXT NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_courses_institutionId ON courses(institutionId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_courses_departmentId ON courses(departmentId)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS classes (
                        classId TEXT NOT NULL PRIMARY KEY,
                        institutionId TEXT NOT NULL,
                        courseId TEXT NOT NULL,
                        teacherId TEXT NOT NULL,
                        name TEXT NOT NULL,
                        studentIds TEXT NOT NULL DEFAULT '',
                        schedule TEXT NOT NULL DEFAULT '',
                        active INTEGER NOT NULL DEFAULT 1
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_classes_institutionId ON classes(institutionId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_classes_courseId ON classes(courseId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_classes_teacherId ON classes(teacherId)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS members (
                        memberId TEXT NOT NULL PRIMARY KEY,
                        institutionId TEXT NOT NULL,
                        userId TEXT NOT NULL,
                        role TEXT NOT NULL,
                        displayName TEXT NOT NULL,
                        email TEXT NOT NULL DEFAULT '',
                        parentUserIds TEXT NOT NULL DEFAULT '',
                        active INTEGER NOT NULL DEFAULT 1
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_members_institutionId ON members(institutionId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_members_userId ON members(userId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_members_role ON members(role)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS assignments (
                        assignmentId TEXT NOT NULL PRIMARY KEY,
                        teacherId TEXT NOT NULL,
                        classId TEXT NOT NULL,
                        title TEXT NOT NULL,
                        description TEXT NOT NULL,
                        assignmentType TEXT NOT NULL,
                        deadline INTEGER NOT NULL,
                        evaluationCriteria TEXT NOT NULL DEFAULT '',
                        targetObjectIds TEXT NOT NULL DEFAULT '',
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_assignments_teacherId ON assignments(teacherId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_assignments_classId ON assignments(classId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_assignments_deadline ON assignments(deadline)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS submissions (
                        submissionId TEXT NOT NULL PRIMARY KEY,
                        assignmentId TEXT NOT NULL,
                        studentId TEXT NOT NULL,
                        status TEXT NOT NULL,
                        score INTEGER,
                        feedback TEXT NOT NULL DEFAULT '',
                        submittedAt INTEGER
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_submissions_assignmentId ON submissions(assignmentId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_submissions_studentId ON submissions(studentId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_submissions_status ON submissions(status)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS classroom_sessions (
                        sessionId TEXT NOT NULL PRIMARY KEY,
                        classId TEXT NOT NULL,
                        teacherId TEXT NOT NULL,
                        title TEXT NOT NULL,
                        sharedObjectIds TEXT NOT NULL DEFAULT '',
                        participantIds TEXT NOT NULL DEFAULT '',
                        status TEXT NOT NULL,
                        startedAt INTEGER,
                        endedAt INTEGER
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_classroom_sessions_classId ON classroom_sessions(classId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_classroom_sessions_teacherId ON classroom_sessions(teacherId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_classroom_sessions_status ON classroom_sessions(status)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS teacher_analytics (
                        reportId TEXT NOT NULL PRIMARY KEY,
                        institutionId TEXT NOT NULL,
                        classId TEXT,
                        completionRate INTEGER NOT NULL,
                        averageQuizScore INTEGER NOT NULL,
                        engagementScore INTEGER NOT NULL,
                        weakAreas TEXT NOT NULL DEFAULT '',
                        learningTrends TEXT NOT NULL DEFAULT '',
                        generatedAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_teacher_analytics_institutionId ON teacher_analytics(institutionId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_teacher_analytics_classId ON teacher_analytics(classId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_teacher_analytics_generatedAt ON teacher_analytics(generatedAt)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS student_reports (
                        reportId TEXT NOT NULL PRIMARY KEY,
                        studentId TEXT NOT NULL,
                        classId TEXT NOT NULL,
                        progressPercent INTEGER NOT NULL,
                        completedLessons INTEGER NOT NULL,
                        averageQuizScore INTEGER NOT NULL,
                        achievements TEXT NOT NULL DEFAULT '',
                        recommendations TEXT NOT NULL DEFAULT '',
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_student_reports_studentId ON student_reports(studentId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_student_reports_classId ON student_reports(classId)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS lms_connections (
                        connectionId TEXT NOT NULL PRIMARY KEY,
                        institutionId TEXT NOT NULL,
                        provider TEXT NOT NULL,
                        status TEXT NOT NULL,
                        syncEnabled INTEGER NOT NULL DEFAULT 0,
                        lastSyncAt INTEGER
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_lms_connections_institutionId ON lms_connections(institutionId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_lms_connections_provider ON lms_connections(provider)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_lms_connections_status ON lms_connections(status)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS enterprise_audit_logs (
                        auditId TEXT NOT NULL PRIMARY KEY,
                        institutionId TEXT NOT NULL,
                        actorUserId TEXT NOT NULL,
                        action TEXT NOT NULL,
                        targetType TEXT NOT NULL,
                        targetId TEXT NOT NULL,
                        timestamp INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_enterprise_audit_logs_institutionId ON enterprise_audit_logs(institutionId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_enterprise_audit_logs_actorUserId ON enterprise_audit_logs(actorUserId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_enterprise_audit_logs_timestamp ON enterprise_audit_logs(timestamp)")
            }
        }

        val MIGRATION_10_11 = object : Migration(10, 11) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS ai_curriculums (
                        curriculumId TEXT NOT NULL PRIMARY KEY,
                        teacherId TEXT NOT NULL,
                        institutionId TEXT NOT NULL,
                        subject TEXT NOT NULL,
                        topic TEXT NOT NULL,
                        gradeLevel TEXT NOT NULL,
                        learningObjective TEXT NOT NULL,
                        durationWeeks INTEGER NOT NULL,
                        studentLevel TEXT NOT NULL,
                        languageCode TEXT NOT NULL,
                        title TEXT NOT NULL,
                        overview TEXT NOT NULL,
                        moduleIds TEXT NOT NULL DEFAULT '',
                        assessmentIds TEXT NOT NULL DEFAULT '',
                        scientificAccuracy REAL NOT NULL,
                        levelAlignment REAL NOT NULL,
                        objectiveCoverage REAL NOT NULL,
                        contentConsistency REAL NOT NULL,
                        qualityNotes TEXT NOT NULL DEFAULT '',
                        approvalStatus TEXT NOT NULL,
                        ownerUserId TEXT NOT NULL,
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_ai_curriculums_teacherId ON ai_curriculums(teacherId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_ai_curriculums_institutionId ON ai_curriculums(institutionId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_ai_curriculums_approvalStatus ON ai_curriculums(approvalStatus)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS curriculum_modules (
                        moduleId TEXT NOT NULL PRIMARY KEY,
                        curriculumId TEXT NOT NULL,
                        title TEXT NOT NULL,
                        objective TEXT NOT NULL,
                        lessonIds TEXT NOT NULL DEFAULT '',
                        estimatedHours INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_curriculum_modules_curriculumId ON curriculum_modules(curriculumId)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS lessons (
                        lessonId TEXT NOT NULL PRIMARY KEY,
                        curriculumId TEXT NOT NULL,
                        title TEXT NOT NULL,
                        objectives TEXT NOT NULL DEFAULT '',
                        explanation TEXT NOT NULL,
                        examples TEXT NOT NULL DEFAULT '',
                        experiments TEXT NOT NULL DEFAULT '',
                        arActivityIds TEXT NOT NULL DEFAULT '',
                        practiceQuestions TEXT NOT NULL DEFAULT '',
                        difficulty TEXT NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_lessons_curriculumId ON lessons(curriculumId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_lessons_difficulty ON lessons(difficulty)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS activities (
                        activityId TEXT NOT NULL PRIMARY KEY,
                        lessonId TEXT NOT NULL,
                        title TEXT NOT NULL,
                        description TEXT NOT NULL,
                        suggestedObjectIds TEXT NOT NULL DEFAULT '',
                        activityType TEXT NOT NULL,
                        estimatedMinutes INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_activities_lessonId ON activities(lessonId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_activities_activityType ON activities(activityType)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS assessments (
                        assessmentId TEXT NOT NULL PRIMARY KEY,
                        curriculumId TEXT NOT NULL,
                        title TEXT NOT NULL,
                        difficulty TEXT NOT NULL,
                        mcqQuestions TEXT NOT NULL DEFAULT '',
                        practicalTasks TEXT NOT NULL DEFAULT '',
                        arAssignments TEXT NOT NULL DEFAULT '',
                        researchActivities TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_assessments_curriculumId ON assessments(curriculumId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_assessments_difficulty ON assessments(difficulty)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS ai_teaching_plans (
                        planId TEXT NOT NULL PRIMARY KEY,
                        teacherId TEXT NOT NULL,
                        studentLevel TEXT NOT NULL,
                        explanationComplexity TEXT NOT NULL,
                        learningSpeed TEXT NOT NULL,
                        exampleStrategy TEXT NOT NULL,
                        activityStrategy TEXT NOT NULL,
                        recommendedInterventions TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_ai_teaching_plans_teacherId ON ai_teaching_plans(teacherId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_ai_teaching_plans_studentLevel ON ai_teaching_plans(studentLevel)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS teacher_reviews (
                        reviewId TEXT NOT NULL PRIMARY KEY,
                        contentId TEXT NOT NULL,
                        teacherId TEXT NOT NULL,
                        status TEXT NOT NULL,
                        comments TEXT NOT NULL,
                        reviewedAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_teacher_reviews_contentId ON teacher_reviews(contentId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_teacher_reviews_teacherId ON teacher_reviews(teacherId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_teacher_reviews_status ON teacher_reviews(status)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS lesson_analytics (
                        analyticsId TEXT NOT NULL PRIMARY KEY,
                        lessonId TEXT NOT NULL,
                        classId TEXT NOT NULL,
                        completionRate INTEGER NOT NULL,
                        engagementScore INTEGER NOT NULL,
                        averageAssessmentScore INTEGER NOT NULL,
                        difficultySignal TEXT NOT NULL,
                        improvementNotes TEXT NOT NULL DEFAULT '',
                        generatedAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_lesson_analytics_lessonId ON lesson_analytics(lessonId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_lesson_analytics_classId ON lesson_analytics(classId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_lesson_analytics_generatedAt ON lesson_analytics(generatedAt)")
            }
        }

        val MIGRATION_11_12 = object : Migration(11, 12) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS agi_memory (
                        eventId TEXT NOT NULL PRIMARY KEY,
                        learnerId TEXT NOT NULL,
                        activityType TEXT NOT NULL,
                        topic TEXT NOT NULL,
                        signal TEXT NOT NULL,
                        score INTEGER,
                        timestamp INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_agi_memory_learnerId ON agi_memory(learnerId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_agi_memory_timestamp ON agi_memory(timestamp)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS learner_models (
                        learnerId TEXT NOT NULL PRIMARY KEY,
                        knowledgeLevel TEXT NOT NULL,
                        learningSpeed TEXT NOT NULL,
                        retentionScore INTEGER NOT NULL,
                        interests TEXT NOT NULL DEFAULT '',
                        previousMistakes TEXT NOT NULL DEFAULT '',
                        learningPatterns TEXT NOT NULL DEFAULT '',
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_learner_models_learnerId ON learner_models(learnerId)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS skill_graphs (
                        skillId TEXT NOT NULL PRIMARY KEY,
                        learnerId TEXT NOT NULL,
                        name TEXT NOT NULL,
                        mastery INTEGER NOT NULL,
                        prerequisites TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_skill_graphs_learnerId ON skill_graphs(learnerId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_skill_graphs_name ON skill_graphs(name)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS ai_decisions (
                        decisionId TEXT NOT NULL PRIMARY KEY,
                        learnerId TEXT NOT NULL,
                        institutionId TEXT NOT NULL,
                        selectedAgents TEXT NOT NULL DEFAULT '',
                        actionTitle TEXT NOT NULL,
                        actionDescription TEXT NOT NULL,
                        verified INTEGER NOT NULL,
                        requiresHumanApproval INTEGER NOT NULL,
                        createdAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_ai_decisions_learnerId ON ai_decisions(learnerId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_ai_decisions_institutionId ON ai_decisions(institutionId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_ai_decisions_createdAt ON ai_decisions(createdAt)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS learning_goals (
                        goalId TEXT NOT NULL PRIMARY KEY,
                        learnerId TEXT NOT NULL,
                        title TEXT NOT NULL,
                        targetSkill TEXT NOT NULL,
                        targetMastery INTEGER NOT NULL,
                        progress INTEGER NOT NULL,
                        dueAt INTEGER
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_learning_goals_learnerId ON learning_goals(learnerId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_learning_goals_targetSkill ON learning_goals(targetSkill)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS future_recommendations (
                        recommendationId TEXT NOT NULL PRIMARY KEY,
                        learnerId TEXT NOT NULL,
                        title TEXT NOT NULL,
                        rationale TEXT NOT NULL,
                        priority TEXT NOT NULL,
                        generatedAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_future_recommendations_learnerId ON future_recommendations(learnerId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_future_recommendations_priority ON future_recommendations(priority)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS knowledge_evolution (
                        proposalId TEXT NOT NULL PRIMARY KEY,
                        institutionId TEXT NOT NULL,
                        topic TEXT NOT NULL,
                        missingConcepts TEXT NOT NULL DEFAULT '',
                        curriculumUpdates TEXT NOT NULL DEFAULT '',
                        requiredApprovalRole TEXT NOT NULL,
                        status TEXT NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_knowledge_evolution_institutionId ON knowledge_evolution(institutionId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_knowledge_evolution_topic ON knowledge_evolution(topic)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_knowledge_evolution_status ON knowledge_evolution(status)")
            }
        }

        val MIGRATION_12_13 = object : Migration(12, 13) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS spatial_worlds (
                        worldId TEXT NOT NULL PRIMARY KEY,
                        title TEXT NOT NULL,
                        environmentType TEXT NOT NULL,
                        subject TEXT NOT NULL,
                        topic TEXT NOT NULL,
                        objective TEXT NOT NULL,
                        classroomId TEXT NOT NULL,
                        objectIds TEXT NOT NULL DEFAULT '',
                        activityIds TEXT NOT NULL DEFAULT '',
                        simulationIds TEXT NOT NULL DEFAULT '',
                        aiTeacherGuidance TEXT NOT NULL DEFAULT '',
                        createdAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_spatial_worlds_subject ON spatial_worlds(subject)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_spatial_worlds_topic ON spatial_worlds(topic)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS digital_twins (
                        twinId TEXT NOT NULL PRIMARY KEY,
                        sourceObjectId TEXT NOT NULL,
                        name TEXT NOT NULL,
                        twinType TEXT NOT NULL,
                        modelUri TEXT NOT NULL,
                        behaviorModel TEXT NOT NULL,
                        explanation TEXT NOT NULL,
                        manipulableProperties TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_digital_twins_sourceObjectId ON digital_twins(sourceObjectId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_digital_twins_twinType ON digital_twins(twinType)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS virtual_classrooms (
                        classroomId TEXT NOT NULL PRIMARY KEY,
                        title TEXT NOT NULL,
                        teacherPresence TEXT NOT NULL,
                        sharedObjectIds TEXT NOT NULL DEFAULT '',
                        lessonFlow TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_virtual_classrooms_teacherPresence ON virtual_classrooms(teacherPresence)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS virtual_lessons (
                        lessonId TEXT NOT NULL PRIMARY KEY,
                        worldId TEXT NOT NULL,
                        topic TEXT NOT NULL,
                        objective TEXT NOT NULL,
                        activityIds TEXT NOT NULL DEFAULT '',
                        assessmentPrompt TEXT NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_virtual_lessons_worldId ON virtual_lessons(worldId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_virtual_lessons_topic ON virtual_lessons(topic)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS simulations (
                        simulationId TEXT NOT NULL PRIMARY KEY,
                        worldId TEXT NOT NULL,
                        title TEXT NOT NULL,
                        simulationType TEXT NOT NULL,
                        parameterNames TEXT NOT NULL DEFAULT '',
                        explanation TEXT NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_simulations_worldId ON simulations(worldId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_simulations_simulationType ON simulations(simulationType)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS spatial_sessions (
                        sessionId TEXT NOT NULL PRIMARY KEY,
                        learnerId TEXT NOT NULL,
                        worldId TEXT NOT NULL,
                        status TEXT NOT NULL,
                        progressPercent INTEGER NOT NULL,
                        startedAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_spatial_sessions_learnerId ON spatial_sessions(learnerId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_spatial_sessions_worldId ON spatial_sessions(worldId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_spatial_sessions_status ON spatial_sessions(status)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS interaction_history (
                        eventId TEXT NOT NULL PRIMARY KEY,
                        sessionId TEXT NOT NULL,
                        objectId TEXT NOT NULL,
                        interactionType TEXT NOT NULL,
                        durationMillis INTEGER NOT NULL,
                        successSignal TEXT NOT NULL,
                        timestamp INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_interaction_history_sessionId ON interaction_history(sessionId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_interaction_history_objectId ON interaction_history(objectId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_interaction_history_timestamp ON interaction_history(timestamp)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS learning_environments (
                        environmentId TEXT NOT NULL PRIMARY KEY,
                        worldId TEXT NOT NULL,
                        environmentType TEXT NOT NULL,
                        roomStructure TEXT NOT NULL,
                        depthQuality REAL NOT NULL,
                        relationships TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_learning_environments_worldId ON learning_environments(worldId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_learning_environments_environmentType ON learning_environments(environmentType)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS spatial_3d_assets (
                        assetId TEXT NOT NULL PRIMARY KEY,
                        name TEXT NOT NULL,
                        uri TEXT NOT NULL,
                        assetType TEXT NOT NULL,
                        optimized INTEGER NOT NULL,
                        storagePath TEXT NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_spatial_3d_assets_assetType ON spatial_3d_assets(assetType)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_spatial_3d_assets_optimized ON spatial_3d_assets(optimized)")
            }
        }

        val MIGRATION_13_14 = object : Migration(13, 14) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS cognitive_profiles (
                        profileId TEXT NOT NULL PRIMARY KEY,
                        userId TEXT NOT NULL,
                        learningLevel TEXT NOT NULL,
                        learningStyle TEXT NOT NULL,
                        knowledgeStrengths TEXT NOT NULL DEFAULT '',
                        knowledgeWeaknesses TEXT NOT NULL DEFAULT '',
                        preferredLearningMethod TEXT NOT NULL,
                        learningSpeed TEXT NOT NULL,
                        memoryAbility TEXT NOT NULL,
                        intelligenceScore INTEGER NOT NULL,
                        cognitiveAnalysisEnabled INTEGER NOT NULL,
                        emotionAnalysisEnabled INTEGER NOT NULL,
                        cloudProcessingEnabled INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_cognitive_profiles_userId ON cognitive_profiles(userId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_cognitive_profiles_learningLevel ON cognitive_profiles(learningLevel)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS cognitive_learner_models (
                        modelId TEXT NOT NULL PRIMARY KEY,
                        userId TEXT NOT NULL,
                        knowledgeSummary TEXT NOT NULL,
                        learningProblemSummary TEXT NOT NULL,
                        preferredStrategy TEXT NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_cognitive_learner_models_userId ON cognitive_learner_models(userId)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS cognitive_memory_records (
                        memoryId TEXT NOT NULL PRIMARY KEY,
                        userId TEXT NOT NULL,
                        memoryType TEXT NOT NULL,
                        topic TEXT NOT NULL,
                        summary TEXT NOT NULL,
                        strength INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_cognitive_memory_records_userId ON cognitive_memory_records(userId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_cognitive_memory_records_memoryType ON cognitive_memory_records(memoryType)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_cognitive_memory_records_topic ON cognitive_memory_records(topic)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS learning_patterns (
                        patternId TEXT NOT NULL PRIMARY KEY,
                        userId TEXT NOT NULL,
                        pattern TEXT NOT NULL,
                        confidence REAL NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_learning_patterns_userId ON learning_patterns(userId)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS behavior_analytics (
                        reportId TEXT NOT NULL PRIMARY KEY,
                        userId TEXT NOT NULL,
                        studyDurationMinutes INTEGER NOT NULL,
                        learningFrequency INTEGER NOT NULL,
                        objectScanningPattern TEXT NOT NULL,
                        quizAttemptPattern TEXT NOT NULL,
                        questionPattern TEXT NOT NULL,
                        contentInteractionPattern TEXT NOT NULL,
                        recommendedMethod TEXT NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_behavior_analytics_userId ON behavior_analytics(userId)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS emotion_analytics (
                        reportId TEXT NOT NULL PRIMARY KEY,
                        userId TEXT NOT NULL,
                        engagement TEXT NOT NULL,
                        frustrationRisk INTEGER NOT NULL,
                        motivation TEXT NOT NULL,
                        confidence INTEGER NOT NULL,
                        recommendedAdjustment TEXT NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_emotion_analytics_userId ON emotion_analytics(userId)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS cognitive_skill_maps (
                        skillId TEXT NOT NULL PRIMARY KEY,
                        userId TEXT NOT NULL,
                        name TEXT NOT NULL,
                        mastery INTEGER NOT NULL,
                        growthTrend TEXT NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_cognitive_skill_maps_userId ON cognitive_skill_maps(userId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_cognitive_skill_maps_name ON cognitive_skill_maps(name)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS learning_predictions (
                        predictionId TEXT NOT NULL PRIMARY KEY,
                        userId TEXT NOT NULL,
                        futurePerformance INTEGER NOT NULL,
                        predictedDifficulties TEXT NOT NULL DEFAULT '',
                        predictedKnowledgeGaps TEXT NOT NULL DEFAULT '',
                        skillImprovement TEXT NOT NULL,
                        requiredLearningPath TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_learning_predictions_userId ON learning_predictions(userId)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS personal_learning_plans (
                        planId TEXT NOT NULL PRIMARY KEY,
                        userId TEXT NOT NULL,
                        dailyGuidance TEXT NOT NULL DEFAULT '',
                        studyPlan TEXT NOT NULL DEFAULT '',
                        motivationalMessage TEXT NOT NULL,
                        weaknessExplanation TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_personal_learning_plans_userId ON personal_learning_plans(userId)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS cognitive_ai_decisions (
                        decisionId TEXT NOT NULL PRIMARY KEY,
                        userId TEXT NOT NULL,
                        nextTopic TEXT NOT NULL,
                        difficultyLevel TEXT NOT NULL,
                        teachingStyle TEXT NOT NULL,
                        assessmentType TEXT NOT NULL,
                        learningEnvironment TEXT NOT NULL,
                        recommendedActivities TEXT NOT NULL DEFAULT '',
                        explanation TEXT NOT NULL,
                        requiresConsent INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_cognitive_ai_decisions_userId ON cognitive_ai_decisions(userId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_cognitive_ai_decisions_nextTopic ON cognitive_ai_decisions(nextTopic)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS cognitive_activity_events (
                        activityId TEXT NOT NULL PRIMARY KEY,
                        userId TEXT NOT NULL,
                        activityType TEXT NOT NULL,
                        topic TEXT NOT NULL,
                        durationMillis INTEGER NOT NULL,
                        score INTEGER,
                        mistake TEXT,
                        timestamp INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_cognitive_activity_events_userId ON cognitive_activity_events(userId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_cognitive_activity_events_activityType ON cognitive_activity_events(activityType)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_cognitive_activity_events_timestamp ON cognitive_activity_events(timestamp)")
            }
        }

        val MIGRATION_14_15 = object : Migration(14, 15) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS robot_profiles (
                        robotId TEXT NOT NULL PRIMARY KEY,
                        name TEXT NOT NULL,
                        robotType TEXT NOT NULL,
                        capabilities TEXT NOT NULL DEFAULT '',
                        sensors TEXT NOT NULL DEFAULT '',
                        teachingModes TEXT NOT NULL DEFAULT '',
                        batteryPercent INTEGER NOT NULL,
                        charging INTEGER NOT NULL,
                        connectionStatus TEXT NOT NULL,
                        learningEnvironment TEXT NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_robot_profiles_robotType ON robot_profiles(robotType)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_robot_profiles_connectionStatus ON robot_profiles(connectionStatus)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS robot_sessions (
                        sessionId TEXT NOT NULL PRIMARY KEY,
                        robotId TEXT NOT NULL,
                        classroomId TEXT NOT NULL,
                        teacherId TEXT NOT NULL,
                        status TEXT NOT NULL,
                        activeTopic TEXT NOT NULL,
                        startedAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_robot_sessions_robotId ON robot_sessions(robotId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_robot_sessions_classroomId ON robot_sessions(classroomId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_robot_sessions_status ON robot_sessions(status)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS robot_interactions (
                        interactionId TEXT NOT NULL PRIMARY KEY,
                        robotId TEXT NOT NULL,
                        studentId TEXT NOT NULL,
                        inputMode TEXT NOT NULL,
                        inputText TEXT NOT NULL,
                        responseText TEXT NOT NULL,
                        emotionResponse TEXT NOT NULL,
                        timestamp INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_robot_interactions_robotId ON robot_interactions(robotId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_robot_interactions_studentId ON robot_interactions(studentId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_robot_interactions_timestamp ON robot_interactions(timestamp)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS robot_memory (
                        memoryId TEXT NOT NULL PRIMARY KEY,
                        robotId TEXT NOT NULL,
                        studentId TEXT NOT NULL,
                        topic TEXT NOT NULL,
                        previousQuestions TEXT NOT NULL DEFAULT '',
                        learningProgress INTEGER NOT NULL,
                        preferences TEXT NOT NULL DEFAULT '',
                        teachingHistory TEXT NOT NULL DEFAULT '',
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_robot_memory_robotId ON robot_memory(robotId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_robot_memory_studentId ON robot_memory(studentId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_robot_memory_topic ON robot_memory(topic)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS robot_teaching_activities (
                        actionId TEXT NOT NULL PRIMARY KEY,
                        robotId TEXT NOT NULL,
                        actionType TEXT NOT NULL,
                        topic TEXT NOT NULL,
                        message TEXT NOT NULL,
                        demonstration TEXT NOT NULL,
                        adaptedDifficulty TEXT NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_robot_teaching_activities_robotId ON robot_teaching_activities(robotId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_robot_teaching_activities_topic ON robot_teaching_activities(topic)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_robot_teaching_activities_actionType ON robot_teaching_activities(actionType)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS robot_classroom_sessions (
                        planId TEXT NOT NULL PRIMARY KEY,
                        robotId TEXT NOT NULL,
                        teacherLesson TEXT NOT NULL,
                        assistanceSteps TEXT NOT NULL DEFAULT '',
                        studentInteractionPrompts TEXT NOT NULL DEFAULT '',
                        analyticsSignals TEXT NOT NULL DEFAULT '',
                        teacherId TEXT NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_robot_classroom_sessions_robotId ON robot_classroom_sessions(robotId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_robot_classroom_sessions_teacherId ON robot_classroom_sessions(teacherId)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS robot_analytics (
                        reportId TEXT NOT NULL PRIMARY KEY,
                        robotId TEXT NOT NULL,
                        studentId TEXT NOT NULL,
                        engagement TEXT NOT NULL,
                        confusionRisk INTEGER NOT NULL,
                        interestLevel INTEGER NOT NULL,
                        confidence INTEGER NOT NULL,
                        robotResponse TEXT NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_robot_analytics_robotId ON robot_analytics(robotId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_robot_analytics_studentId ON robot_analytics(studentId)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS student_robot_history (
                        historyId TEXT NOT NULL PRIMARY KEY,
                        studentId TEXT NOT NULL,
                        robotId TEXT NOT NULL,
                        topics TEXT NOT NULL DEFAULT '',
                        decisions TEXT NOT NULL DEFAULT '',
                        lastInteractionAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_student_robot_history_studentId ON student_robot_history(studentId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_student_robot_history_robotId ON student_robot_history(robotId)")
            }
        }

        val MIGRATION_15_16 = object : Migration(15, 16) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS neural_profiles (
                        profileId TEXT NOT NULL PRIMARY KEY,
                        userId TEXT NOT NULL,
                        cognitivePatterns TEXT NOT NULL DEFAULT '',
                        attentionBehavior TEXT NOT NULL,
                        memoryResponse TEXT NOT NULL,
                        learningSpeed TEXT NOT NULL,
                        preferredTeachingMethod TEXT NOT NULL,
                        knowledgeDevelopment TEXT NOT NULL DEFAULT '',
                        consentGranted INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_neural_profiles_userId ON neural_profiles(userId)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS brain_signals (
                        signalId TEXT NOT NULL PRIMARY KEY,
                        userId TEXT NOT NULL,
                        sessionId TEXT NOT NULL,
                        attentionScore REAL NOT NULL,
                        engagementScore REAL NOT NULL,
                        mentalWorkloadScore REAL NOT NULL,
                        fatigueScore REAL NOT NULL,
                        signalQuality TEXT NOT NULL,
                        timestamp INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_brain_signals_userId ON brain_signals(userId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_brain_signals_sessionId ON brain_signals(sessionId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_brain_signals_timestamp ON brain_signals(timestamp)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS neural_cognitive_states (
                        stateId TEXT NOT NULL PRIMARY KEY,
                        userId TEXT NOT NULL,
                        topic TEXT NOT NULL,
                        attentionPercent INTEGER NOT NULL,
                        engagementPercent INTEGER NOT NULL,
                        focusLevel TEXT NOT NULL,
                        cognitiveLoadLevel TEXT NOT NULL,
                        understandingLevel TEXT NOT NULL,
                        mentalFatiguePercent INTEGER NOT NULL,
                        explanation TEXT NOT NULL,
                        timestamp INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_neural_cognitive_states_userId ON neural_cognitive_states(userId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_neural_cognitive_states_topic ON neural_cognitive_states(topic)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_neural_cognitive_states_timestamp ON neural_cognitive_states(timestamp)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS neural_learning_states (
                        stateId TEXT NOT NULL PRIMARY KEY,
                        userId TEXT NOT NULL,
                        activeTopic TEXT NOT NULL,
                        recommendedDifficulty INTEGER NOT NULL,
                        recommendedMethod TEXT NOT NULL,
                        supportRequired INTEGER NOT NULL,
                        adaptationReason TEXT NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_neural_learning_states_userId ON neural_learning_states(userId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_neural_learning_states_activeTopic ON neural_learning_states(activeTopic)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS neural_interactions (
                        responseId TEXT NOT NULL PRIMARY KEY,
                        userId TEXT NOT NULL,
                        topic TEXT NOT NULL,
                        actions TEXT NOT NULL DEFAULT '',
                        teacherPrompt TEXT NOT NULL,
                        explainableReason TEXT NOT NULL,
                        timestamp INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_neural_interactions_userId ON neural_interactions(userId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_neural_interactions_topic ON neural_interactions(topic)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_neural_interactions_timestamp ON neural_interactions(timestamp)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS attention_records (
                        recordId TEXT NOT NULL PRIMARY KEY,
                        userId TEXT NOT NULL,
                        topic TEXT NOT NULL,
                        attentionPercent INTEGER NOT NULL,
                        engagementPercent INTEGER NOT NULL,
                        focusLevel TEXT NOT NULL,
                        timestamp INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_attention_records_userId ON attention_records(userId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_attention_records_topic ON attention_records(topic)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_attention_records_timestamp ON attention_records(timestamp)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS neural_learning_predictions (
                        predictionId TEXT NOT NULL PRIMARY KEY,
                        userId TEXT NOT NULL,
                        topic TEXT NOT NULL,
                        learningSuccessPercent INTEGER NOT NULL,
                        requiredSupport TEXT NOT NULL DEFAULT '',
                        skillDevelopment TEXT NOT NULL DEFAULT '',
                        knowledgeRetentionPercent INTEGER NOT NULL,
                        longTermRoadmap TEXT NOT NULL DEFAULT '',
                        explanation TEXT NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_neural_learning_predictions_userId ON neural_learning_predictions(userId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_neural_learning_predictions_topic ON neural_learning_predictions(topic)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS cognitive_reports (
                        reportId TEXT NOT NULL PRIMARY KEY,
                        userId TEXT NOT NULL,
                        summary TEXT NOT NULL,
                        recommendations TEXT NOT NULL DEFAULT '',
                        privacyMode TEXT NOT NULL,
                        createdAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_cognitive_reports_userId ON cognitive_reports(userId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_cognitive_reports_createdAt ON cognitive_reports(createdAt)")
            }
        }

        val MIGRATION_16_17 = object : Migration(16, 17) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS agi_network_agents (
                        agentId TEXT NOT NULL PRIMARY KEY,
                        role TEXT NOT NULL,
                        name TEXT NOT NULL,
                        capabilities TEXT NOT NULL DEFAULT '',
                        active INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_agi_network_agents_role ON agi_network_agents(role)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_agi_network_agents_active ON agi_network_agents(active)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS agi_network_agent_tasks (
                        taskId TEXT NOT NULL PRIMARY KEY,
                        agentRole TEXT NOT NULL,
                        title TEXT NOT NULL,
                        priority INTEGER NOT NULL,
                        status TEXT NOT NULL,
                        evidence TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_agi_network_agent_tasks_agentRole ON agi_network_agent_tasks(agentRole)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_agi_network_agent_tasks_status ON agi_network_agent_tasks(status)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS agi_network_agent_communication (
                        messageId TEXT NOT NULL PRIMARY KEY,
                        fromAgent TEXT NOT NULL,
                        toAgent TEXT NOT NULL,
                        topic TEXT NOT NULL,
                        content TEXT NOT NULL,
                        confidence REAL NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_agi_network_agent_communication_topic ON agi_network_agent_communication(topic)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_agi_network_agent_communication_fromAgent ON agi_network_agent_communication(fromAgent)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_agi_network_agent_communication_toAgent ON agi_network_agent_communication(toAgent)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS agi_network_ai_evolution_history (
                        evaluationId TEXT NOT NULL PRIMARY KEY,
                        teachingImprovement TEXT NOT NULL,
                        recommendationAccuracyPercent INTEGER NOT NULL,
                        questionQualityPercent INTEGER NOT NULL,
                        contentQualityPercent INTEGER NOT NULL,
                        improvementTargets TEXT NOT NULL DEFAULT '',
                        createdAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_agi_network_ai_evolution_history_createdAt ON agi_network_ai_evolution_history(createdAt)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS agi_network_knowledge_evolution (
                        proposalId TEXT NOT NULL PRIMARY KEY,
                        topic TEXT NOT NULL,
                        missingConcepts TEXT NOT NULL DEFAULT '',
                        improvedRelationships TEXT NOT NULL DEFAULT '',
                        materialUpdates TEXT NOT NULL DEFAULT '',
                        verificationEvidence TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_agi_network_knowledge_evolution_topic ON agi_network_knowledge_evolution(topic)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS agi_network_ai_decisions (
                        decisionId TEXT NOT NULL PRIMARY KEY,
                        learnerId TEXT NOT NULL,
                        topic TEXT NOT NULL,
                        teachingApproach TEXT NOT NULL,
                        requiredContent TEXT NOT NULL DEFAULT '',
                        difficultyAdjustment TEXT NOT NULL,
                        learningEnvironment TEXT NOT NULL,
                        assessmentStrategy TEXT NOT NULL,
                        explanation TEXT NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_agi_network_ai_decisions_learnerId ON agi_network_ai_decisions(learnerId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_agi_network_ai_decisions_topic ON agi_network_ai_decisions(topic)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS agi_network_curriculum_evolution (
                        planId TEXT NOT NULL PRIMARY KEY,
                        topic TEXT NOT NULL,
                        missingSkills TEXT NOT NULL DEFAULT '',
                        generatedCourses TEXT NOT NULL DEFAULT '',
                        updateRecommendations TEXT NOT NULL DEFAULT '',
                        approvalRequired INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_agi_network_curriculum_evolution_topic ON agi_network_curriculum_evolution(topic)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_agi_network_curriculum_evolution_approvalRequired ON agi_network_curriculum_evolution(approvalRequired)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS agi_network_analytics (
                        reportId TEXT NOT NULL PRIMARY KEY,
                        institutionId TEXT NOT NULL,
                        globalPatterns TEXT NOT NULL DEFAULT '',
                        aiPerformancePercent INTEGER NOT NULL,
                        studentSuccessPercent INTEGER NOT NULL,
                        knowledgeGrowth TEXT NOT NULL DEFAULT '',
                        recommendations TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_agi_network_analytics_institutionId ON agi_network_analytics(institutionId)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS agi_network_governance_records (
                        recordId TEXT NOT NULL PRIMARY KEY,
                        decision TEXT NOT NULL,
                        humanApprovalRequired INTEGER NOT NULL,
                        transparencyNotes TEXT NOT NULL DEFAULT '',
                        safetyRules TEXT NOT NULL DEFAULT '',
                        auditSummary TEXT NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_agi_network_governance_records_decision ON agi_network_governance_records(decision)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_agi_network_governance_records_humanApprovalRequired ON agi_network_governance_records(humanApprovalRequired)")
            }
        }

        val MIGRATION_17_18 = object : Migration(17, 18) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS quantum_profiles (
                        profileId TEXT NOT NULL PRIMARY KEY,
                        learnerId TEXT NOT NULL,
                        computeMode TEXT NOT NULL,
                        optimizationReadinessPercent INTEGER NOT NULL,
                        preferredExplanationStyle TEXT NOT NULL,
                        activeGoals TEXT NOT NULL DEFAULT '',
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_quantum_profiles_learnerId ON quantum_profiles(learnerId)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS quantum_models (
                        modelId TEXT NOT NULL PRIMARY KEY,
                        name TEXT NOT NULL,
                        computeMode TEXT NOT NULL,
                        version TEXT NOT NULL,
                        optimizationScope TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_quantum_models_computeMode ON quantum_models(computeMode)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS optimization_results (
                        optimizationId TEXT NOT NULL PRIMARY KEY,
                        learnerId TEXT NOT NULL,
                        topic TEXT NOT NULL,
                        learningOptimizationScore INTEGER NOT NULL,
                        optimizedPath TEXT NOT NULL DEFAULT '',
                        curriculumSequence TEXT NOT NULL DEFAULT '',
                        assessmentStrategy TEXT NOT NULL,
                        recommendationStrategy TEXT NOT NULL,
                        explanation TEXT NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_optimization_results_learnerId ON optimization_results(learnerId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_optimization_results_topic ON optimization_results(topic)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS quantum_decisions (
                        decisionId TEXT NOT NULL PRIMARY KEY,
                        learnerId TEXT NOT NULL,
                        topic TEXT NOT NULL,
                        decisionType TEXT NOT NULL,
                        educationalAction TEXT NOT NULL,
                        confidencePercent INTEGER NOT NULL,
                        explanation TEXT NOT NULL,
                        humanControlRequired INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_quantum_decisions_learnerId ON quantum_decisions(learnerId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_quantum_decisions_topic ON quantum_decisions(topic)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_quantum_decisions_decisionType ON quantum_decisions(decisionType)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS learning_optimization_history (
                        historyId TEXT NOT NULL PRIMARY KEY,
                        learnerId TEXT NOT NULL,
                        topic TEXT NOT NULL,
                        summary TEXT NOT NULL,
                        score INTEGER NOT NULL,
                        createdAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_learning_optimization_history_learnerId ON learning_optimization_history(learnerId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_learning_optimization_history_createdAt ON learning_optimization_history(createdAt)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS quantum_predictions (
                        predictionId TEXT NOT NULL PRIMARY KEY,
                        learnerId TEXT NOT NULL,
                        topic TEXT NOT NULL,
                        futurePerformancePercent INTEGER NOT NULL,
                        skillDevelopment TEXT NOT NULL DEFAULT '',
                        learningChallenges TEXT NOT NULL DEFAULT '',
                        knowledgeRequirements TEXT NOT NULL DEFAULT '',
                        longTermRoadmap TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_quantum_predictions_learnerId ON quantum_predictions(learnerId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_quantum_predictions_topic ON quantum_predictions(topic)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS knowledge_discovery_records (
                        discoveryId TEXT NOT NULL PRIMARY KEY,
                        topic TEXT NOT NULL,
                        hiddenRelationships TEXT NOT NULL DEFAULT '',
                        discoveredConcepts TEXT NOT NULL DEFAULT '',
                        scientificSignals TEXT NOT NULL DEFAULT '',
                        expansionRecommendation TEXT NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_knowledge_discovery_records_topic ON knowledge_discovery_records(topic)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS quantum_analytics (
                        reportId TEXT NOT NULL PRIMARY KEY,
                        institutionId TEXT NOT NULL,
                        learningOptimizationScore INTEGER NOT NULL,
                        aiImprovementPercent INTEGER NOT NULL,
                        predictionAccuracyPercent INTEGER NOT NULL,
                        systemIntelligenceGrowth TEXT NOT NULL DEFAULT '',
                        auditNotes TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_quantum_analytics_institutionId ON quantum_analytics(institutionId)")
            }
        }

        val MIGRATION_18_19 = object : Migration(18, 19) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS asi_profiles (
                        profileId TEXT NOT NULL PRIMARY KEY,
                        learnerId TEXT NOT NULL,
                        intelligenceScope TEXT NOT NULL DEFAULT '',
                        personalizationDepthPercent INTEGER NOT NULL,
                        responsibleAIMode TEXT NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_asi_profiles_learnerId ON asi_profiles(learnerId)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS asi_models (
                        modelId TEXT NOT NULL PRIMARY KEY,
                        name TEXT NOT NULL,
                        capabilities TEXT NOT NULL DEFAULT '',
                        safetyBoundary TEXT NOT NULL,
                        version TEXT NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS reasoning_history (
                        traceId TEXT NOT NULL PRIMARY KEY,
                        topic TEXT NOT NULL,
                        reasoningSteps TEXT NOT NULL DEFAULT '',
                        confidencePercent INTEGER NOT NULL,
                        explanation TEXT NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_reasoning_history_topic ON reasoning_history(topic)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS knowledge_evolution_records (
                        mapId TEXT NOT NULL PRIMARY KEY,
                        topic TEXT NOT NULL,
                        domainConnections TEXT NOT NULL DEFAULT '',
                        newKnowledgeLinks TEXT NOT NULL DEFAULT '',
                        contentImprovementIdeas TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_knowledge_evolution_records_topic ON knowledge_evolution_records(topic)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS self_improvement_logs (
                        logId TEXT NOT NULL PRIMARY KEY,
                        improvedAreas TEXT NOT NULL DEFAULT '',
                        evaluationSummary TEXT NOT NULL,
                        requiresOfflineValidation INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_self_improvement_logs_requiresOfflineValidation ON self_improvement_logs(requiresOfflineValidation)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS ai_creative_outputs (
                        outputId TEXT NOT NULL PRIMARY KEY,
                        topic TEXT NOT NULL,
                        educationalApproaches TEXT NOT NULL DEFAULT '',
                        learningActivities TEXT NOT NULL DEFAULT '',
                        simulations TEXT NOT NULL DEFAULT '',
                        researchDirections TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_ai_creative_outputs_topic ON ai_creative_outputs(topic)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS human_ai_interactions (
                        interactionId TEXT NOT NULL PRIMARY KEY,
                        planId TEXT NOT NULL,
                        stakeholders TEXT NOT NULL DEFAULT '',
                        aiSuggestions TEXT NOT NULL DEFAULT '',
                        requiredApprovals TEXT NOT NULL DEFAULT '',
                        feedbackLoop TEXT NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_human_ai_interactions_planId ON human_ai_interactions(planId)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS asi_governance_records (
                        recordId TEXT NOT NULL PRIMARY KEY,
                        approvalStatus TEXT NOT NULL,
                        riskLevel TEXT NOT NULL,
                        transparencyNotes TEXT NOT NULL DEFAULT '',
                        ethicsChecks TEXT NOT NULL DEFAULT '',
                        humanOverrideAvailable INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_asi_governance_records_approvalStatus ON asi_governance_records(approvalStatus)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_asi_governance_records_riskLevel ON asi_governance_records(riskLevel)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS global_education_insights (
                        insightId TEXT NOT NULL PRIMARY KEY,
                        institutionId TEXT NOT NULL,
                        globalEducationPatterns TEXT NOT NULL DEFAULT '',
                        knowledgeSharingPlan TEXT NOT NULL,
                        innovationOpportunities TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_global_education_insights_institutionId ON global_education_insights(institutionId)")
            }
        }

        val MIGRATION_19_20 = object : Migration(19, 20) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS global_education_network (
                        networkId TEXT NOT NULL PRIMARY KEY,
                        institutionId TEXT NOT NULL,
                        region TEXT NOT NULL,
                        connectedParticipants TEXT NOT NULL DEFAULT '',
                        sharedKnowledgeTopics TEXT NOT NULL DEFAULT '',
                        learningImprovementPlan TEXT NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_global_education_network_institutionId ON global_education_network(institutionId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_global_education_network_region ON global_education_network(region)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS knowledge_communities (
                        communityId TEXT NOT NULL PRIMARY KEY,
                        collaborationGroups TEXT NOT NULL DEFAULT '',
                        sharedResources TEXT NOT NULL DEFAULT '',
                        aiRecommendations TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_knowledge_communities_communityId ON knowledge_communities(communityId)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS ai_agents (
                        agentId TEXT NOT NULL PRIMARY KEY,
                        agentType TEXT NOT NULL,
                        capabilities TEXT NOT NULL DEFAULT '',
                        trustLevel TEXT NOT NULL,
                        authenticated INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_ai_agents_agentType ON ai_agents(agentType)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_ai_agents_trustLevel ON ai_agents(trustLevel)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS education_institutions (
                        institutionId TEXT NOT NULL PRIMARY KEY,
                        region TEXT NOT NULL,
                        languages TEXT NOT NULL DEFAULT '',
                        activeServices TEXT NOT NULL DEFAULT '',
                        accessibilityImprovements TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_education_institutions_institutionId ON education_institutions(institutionId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_education_institutions_region ON education_institutions(region)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS innovation_records (
                        innovationId TEXT NOT NULL PRIMARY KEY,
                        topic TEXT NOT NULL,
                        contentIdeas TEXT NOT NULL DEFAULT '',
                        researchCollaborations TEXT NOT NULL DEFAULT '',
                        exchangeValue TEXT NOT NULL,
                        humanApprovalRequired INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_innovation_records_topic ON innovation_records(topic)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS global_learning_analytics (
                        reportId TEXT NOT NULL PRIMARY KEY,
                        institutionId TEXT NOT NULL,
                        worldwideTrends TEXT NOT NULL DEFAULT '',
                        knowledgeGaps TEXT NOT NULL DEFAULT '',
                        futureSkillNeeds TEXT NOT NULL DEFAULT '',
                        improvementSummary TEXT NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_global_learning_analytics_institutionId ON global_learning_analytics(institutionId)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS digital_avatars (
                        avatarId TEXT NOT NULL PRIMARY KEY,
                        learnerId TEXT NOT NULL,
                        learningHistory TEXT NOT NULL DEFAULT '',
                        skills TEXT NOT NULL DEFAULT '',
                        knowledgeLevel TEXT NOT NULL,
                        goals TEXT NOT NULL DEFAULT '',
                        achievements TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_digital_avatars_learnerId ON digital_avatars(learnerId)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS governance_policies (
                        policyId TEXT NOT NULL PRIMARY KEY,
                        decision TEXT NOT NULL,
                        trustLevel TEXT NOT NULL,
                        accountabilityRules TEXT NOT NULL DEFAULT '',
                        dataProtectionRules TEXT NOT NULL DEFAULT '',
                        auditSummary TEXT NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_governance_policies_decision ON governance_policies(decision)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_governance_policies_trustLevel ON governance_policies(trustLevel)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS knowledge_exchange_history (
                        exchangeId TEXT NOT NULL PRIMARY KEY,
                        topic TEXT NOT NULL,
                        validationSteps TEXT NOT NULL DEFAULT '',
                        distributionReason TEXT NOT NULL,
                        createdAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_knowledge_exchange_history_topic ON knowledge_exchange_history(topic)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_knowledge_exchange_history_createdAt ON knowledge_exchange_history(createdAt)")
            }
        }

        val MIGRATION_20_21 = object : Migration(20, 21) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS metaverse_virtual_worlds (
                        worldId TEXT NOT NULL PRIMARY KEY,
                        institutionId TEXT NOT NULL,
                        title TEXT NOT NULL,
                        worldType TEXT NOT NULL,
                        subject TEXT NOT NULL,
                        topic TEXT NOT NULL,
                        persistent INTEGER NOT NULL,
                        spaces TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_metaverse_virtual_worlds_institutionId ON metaverse_virtual_worlds(institutionId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_metaverse_virtual_worlds_topic ON metaverse_virtual_worlds(topic)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS metaverse_digital_spaces (
                        spaceId TEXT NOT NULL PRIMARY KEY,
                        worldId TEXT NOT NULL,
                        name TEXT NOT NULL,
                        spaceType TEXT NOT NULL,
                        interactiveObjects TEXT NOT NULL DEFAULT '',
                        learningActivities TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_metaverse_digital_spaces_worldId ON metaverse_digital_spaces(worldId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_metaverse_digital_spaces_spaceType ON metaverse_digital_spaces(spaceType)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS metaverse_learning_avatars (
                        avatarId TEXT NOT NULL PRIMARY KEY,
                        learnerId TEXT NOT NULL,
                        displayName TEXT NOT NULL,
                        role TEXT NOT NULL,
                        learningHistory TEXT NOT NULL DEFAULT '',
                        skills TEXT NOT NULL DEFAULT '',
                        achievements TEXT NOT NULL DEFAULT '',
                        knowledgeLevel TEXT NOT NULL,
                        personalityProfile TEXT NOT NULL,
                        learningGoals TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_metaverse_learning_avatars_learnerId ON metaverse_learning_avatars(learnerId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_metaverse_learning_avatars_role ON metaverse_learning_avatars(role)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS metaverse_virtual_classrooms (
                        classroomId TEXT NOT NULL PRIMARY KEY,
                        worldId TEXT NOT NULL,
                        title TEXT NOT NULL,
                        participants TEXT NOT NULL DEFAULT '',
                        sharedObjects TEXT NOT NULL DEFAULT '',
                        lessonFlow TEXT NOT NULL DEFAULT '',
                        analyticsSignals TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_metaverse_virtual_classrooms_worldId ON metaverse_virtual_classrooms(worldId)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS metaverse_sessions (
                        sessionId TEXT NOT NULL PRIMARY KEY,
                        learnerId TEXT NOT NULL,
                        worldId TEXT NOT NULL,
                        classroomId TEXT NOT NULL,
                        status TEXT NOT NULL,
                        startedAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_metaverse_sessions_learnerId ON metaverse_sessions(learnerId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_metaverse_sessions_worldId ON metaverse_sessions(worldId)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS metaverse_avatar_interactions (
                        interactionId TEXT NOT NULL PRIMARY KEY,
                        avatarId TEXT NOT NULL,
                        observedAction TEXT NOT NULL,
                        environmentResponse TEXT NOT NULL,
                        learningImprovement TEXT NOT NULL,
                        timestamp INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_metaverse_avatar_interactions_avatarId ON metaverse_avatar_interactions(avatarId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_metaverse_avatar_interactions_timestamp ON metaverse_avatar_interactions(timestamp)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS metaverse_virtual_experiments (
                        experimentId TEXT NOT NULL PRIMARY KEY,
                        worldId TEXT NOT NULL,
                        topic TEXT NOT NULL,
                        manipulableSystems TEXT NOT NULL DEFAULT '',
                        assessment TEXT NOT NULL,
                        simulationAccuracy TEXT NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_metaverse_virtual_experiments_worldId ON metaverse_virtual_experiments(worldId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_metaverse_virtual_experiments_topic ON metaverse_virtual_experiments(topic)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS metaverse_community_spaces (
                        communityId TEXT NOT NULL PRIMARY KEY,
                        discussionSpaces TEXT NOT NULL DEFAULT '',
                        collaborativeProjects TEXT NOT NULL DEFAULT '',
                        sharedKnowledge TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_metaverse_community_spaces_communityId ON metaverse_community_spaces(communityId)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS metaverse_analytics (
                        reportId TEXT NOT NULL PRIMARY KEY,
                        learnerId TEXT NOT NULL,
                        explorationScore INTEGER NOT NULL,
                        collaborationScore INTEGER NOT NULL,
                        engagementScore INTEGER NOT NULL,
                        performanceSummary TEXT NOT NULL,
                        governanceDecision TEXT NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_metaverse_analytics_learnerId ON metaverse_analytics(learnerId)")
            }
        }
    }
}
