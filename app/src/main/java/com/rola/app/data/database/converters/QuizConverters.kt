package com.rola.app.data.database.converters

import androidx.room.TypeConverter
import com.rola.app.domain.model.AGIAgentRole
import com.rola.app.domain.model.AGIActivityType
import com.rola.app.domain.model.ChatRole
import com.rola.app.domain.model.CognitiveActivityType
import com.rola.app.domain.model.CognitiveLearningStyle
import com.rola.app.domain.model.CognitiveMemoryType
import com.rola.app.domain.model.DigitalTwinType
import com.rola.app.domain.model.EducationalRobotType
import com.rola.app.domain.model.KnowledgeNodeType
import com.rola.app.domain.model.KnowledgeRelationType
import com.rola.app.domain.model.LearningStatus
import com.rola.app.domain.model.MemoryAbility
import com.rola.app.domain.model.PreferredLearningMethod
import com.rola.app.domain.model.QuizDifficulty
import com.rola.app.domain.model.RecommendationPriority
import com.rola.app.domain.model.RobotConnectionStatus
import com.rola.app.domain.model.RobotDecisionType
import com.rola.app.domain.model.RobotInputMode
import com.rola.app.domain.model.RobotSessionStatus
import com.rola.app.domain.model.RobotTeachingActionType
import com.rola.app.domain.model.SkillLevel
import com.rola.app.domain.model.SpatialEnvironmentType
import com.rola.app.domain.model.SpatialInteractionType
import com.rola.app.domain.model.SpatialSessionStatus
import com.rola.app.domain.model.SpatialSimulationType

class QuizConverters {
    @TypeConverter
    fun fromDifficulty(value: QuizDifficulty): String = value.name

    @TypeConverter
    fun toDifficulty(value: String): QuizDifficulty =
        runCatching { QuizDifficulty.valueOf(value) }.getOrDefault(QuizDifficulty.Easy)

    @TypeConverter
    fun fromLearningStatus(value: LearningStatus): String = value.name

    @TypeConverter
    fun toLearningStatus(value: String): LearningStatus =
        runCatching { LearningStatus.valueOf(value) }.getOrDefault(LearningStatus.Scanned)

    @TypeConverter
    fun fromChatRole(value: ChatRole): String = value.name

    @TypeConverter
    fun toChatRole(value: String): ChatRole =
        runCatching { ChatRole.valueOf(value) }.getOrDefault(ChatRole.Assistant)

    @TypeConverter
    fun fromSkillLevel(value: SkillLevel): String = value.name

    @TypeConverter
    fun toSkillLevel(value: String): SkillLevel =
        runCatching { SkillLevel.valueOf(value) }.getOrDefault(SkillLevel.Beginner)

    @TypeConverter
    fun fromKnowledgeNodeType(value: KnowledgeNodeType): String = value.name

    @TypeConverter
    fun toKnowledgeNodeType(value: String): KnowledgeNodeType =
        runCatching { KnowledgeNodeType.valueOf(value) }.getOrDefault(KnowledgeNodeType.Object)

    @TypeConverter
    fun fromKnowledgeRelationType(value: KnowledgeRelationType): String = value.name

    @TypeConverter
    fun toKnowledgeRelationType(value: String): KnowledgeRelationType =
        runCatching { KnowledgeRelationType.valueOf(value) }.getOrDefault(KnowledgeRelationType.RelatedTo)

    @TypeConverter
    fun fromAGIActivityType(value: AGIActivityType): String = value.name

    @TypeConverter
    fun toAGIActivityType(value: String): AGIActivityType =
        runCatching { AGIActivityType.valueOf(value) }.getOrDefault(AGIActivityType.LessonComplete)

    @TypeConverter
    fun fromRecommendationPriority(value: RecommendationPriority): String = value.name

    @TypeConverter
    fun toRecommendationPriority(value: String): RecommendationPriority =
        runCatching { RecommendationPriority.valueOf(value) }.getOrDefault(RecommendationPriority.Medium)

    @TypeConverter
    fun fromAGIAgentRole(value: AGIAgentRole): String = value.name

    @TypeConverter
    fun toAGIAgentRole(value: String): AGIAgentRole =
        runCatching { AGIAgentRole.valueOf(value) }.getOrDefault(AGIAgentRole.PersonalMentorAgent)

    @TypeConverter
    fun fromSpatialEnvironmentType(value: SpatialEnvironmentType): String = value.name

    @TypeConverter
    fun toSpatialEnvironmentType(value: String): SpatialEnvironmentType =
        runCatching { SpatialEnvironmentType.valueOf(value) }.getOrDefault(SpatialEnvironmentType.VirtualClassroom)

    @TypeConverter
    fun fromSpatialInteractionType(value: SpatialInteractionType): String = value.name

    @TypeConverter
    fun toSpatialInteractionType(value: String): SpatialInteractionType =
        runCatching { SpatialInteractionType.valueOf(value) }.getOrDefault(SpatialInteractionType.Select)

    @TypeConverter
    fun fromSpatialSessionStatus(value: SpatialSessionStatus): String = value.name

    @TypeConverter
    fun toSpatialSessionStatus(value: String): SpatialSessionStatus =
        runCatching { SpatialSessionStatus.valueOf(value) }.getOrDefault(SpatialSessionStatus.Entered)

    @TypeConverter
    fun fromSpatialSimulationType(value: SpatialSimulationType): String = value.name

    @TypeConverter
    fun toSpatialSimulationType(value: String): SpatialSimulationType =
        runCatching { SpatialSimulationType.valueOf(value) }.getOrDefault(SpatialSimulationType.Physics)

    @TypeConverter
    fun fromDigitalTwinType(value: DigitalTwinType): String = value.name

    @TypeConverter
    fun toDigitalTwinType(value: String): DigitalTwinType =
        runCatching { DigitalTwinType.valueOf(value) }.getOrDefault(DigitalTwinType.RealObject)

    @TypeConverter
    fun fromCognitiveLearningStyle(value: CognitiveLearningStyle): String = value.name

    @TypeConverter
    fun toCognitiveLearningStyle(value: String): CognitiveLearningStyle =
        runCatching { CognitiveLearningStyle.valueOf(value) }.getOrDefault(CognitiveLearningStyle.Visual)

    @TypeConverter
    fun fromPreferredLearningMethod(value: PreferredLearningMethod): String = value.name

    @TypeConverter
    fun toPreferredLearningMethod(value: String): PreferredLearningMethod =
        runCatching { PreferredLearningMethod.valueOf(value) }.getOrDefault(PreferredLearningMethod.ARModel)

    @TypeConverter
    fun fromMemoryAbility(value: MemoryAbility): String = value.name

    @TypeConverter
    fun toMemoryAbility(value: String): MemoryAbility =
        runCatching { MemoryAbility.valueOf(value) }.getOrDefault(MemoryAbility.Developing)

    @TypeConverter
    fun fromCognitiveMemoryType(value: CognitiveMemoryType): String = value.name

    @TypeConverter
    fun toCognitiveMemoryType(value: String): CognitiveMemoryType =
        runCatching { CognitiveMemoryType.valueOf(value) }.getOrDefault(CognitiveMemoryType.LearningPattern)

    @TypeConverter
    fun fromCognitiveActivityType(value: CognitiveActivityType): String = value.name

    @TypeConverter
    fun toCognitiveActivityType(value: String): CognitiveActivityType =
        runCatching { CognitiveActivityType.valueOf(value) }.getOrDefault(CognitiveActivityType.Lesson)

    @TypeConverter
    fun fromEducationalRobotType(value: EducationalRobotType): String = value.name

    @TypeConverter
    fun toEducationalRobotType(value: String): EducationalRobotType =
        runCatching { EducationalRobotType.valueOf(value) }.getOrDefault(EducationalRobotType.ClassroomRobot)

    @TypeConverter
    fun fromRobotConnectionStatus(value: RobotConnectionStatus): String = value.name

    @TypeConverter
    fun toRobotConnectionStatus(value: String): RobotConnectionStatus =
        runCatching { RobotConnectionStatus.valueOf(value) }.getOrDefault(RobotConnectionStatus.Offline)

    @TypeConverter
    fun fromRobotInputMode(value: RobotInputMode): String = value.name

    @TypeConverter
    fun toRobotInputMode(value: String): RobotInputMode =
        runCatching { RobotInputMode.valueOf(value) }.getOrDefault(RobotInputMode.Voice)

    @TypeConverter
    fun fromRobotSessionStatus(value: RobotSessionStatus): String = value.name

    @TypeConverter
    fun toRobotSessionStatus(value: String): RobotSessionStatus =
        runCatching { RobotSessionStatus.valueOf(value) }.getOrDefault(RobotSessionStatus.Preparing)

    @TypeConverter
    fun fromRobotTeachingActionType(value: RobotTeachingActionType): String = value.name

    @TypeConverter
    fun toRobotTeachingActionType(value: String): RobotTeachingActionType =
        runCatching { RobotTeachingActionType.valueOf(value) }.getOrDefault(RobotTeachingActionType.Explain)

    @TypeConverter
    fun fromRobotDecisionType(value: RobotDecisionType): String = value.name

    @TypeConverter
    fun toRobotDecisionType(value: String): RobotDecisionType =
        runCatching { RobotDecisionType.valueOf(value) }.getOrDefault(RobotDecisionType.ExplainNow)
}
