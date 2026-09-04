package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rola.app.domain.model.LearningProfile
import com.rola.app.domain.model.LearningSpeed
import com.rola.app.domain.model.SkillLevel

@Entity(tableName = "learning_profiles")
data class LearningProfileEntity(
    @PrimaryKey val userId: String,
    val totalObjectsLearned: Int,
    val averageQuizScore: Int,
    val favoriteCategories: List<String>,
    val weakAreas: List<String>,
    val learningLevel: SkillLevel,
    val learningStreak: Int,
    val totalLearningTimeMillis: Long,
    val frequentlySearchedTopics: List<String>,
    val difficultConcepts: List<String>,
    val preferredLanguage: String,
    val learningSpeed: LearningSpeed,
    val personalizationEnabled: Boolean = true,
    val isSynced: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis(),
) {
    fun toDomain(): LearningProfile = LearningProfile(
        userId = userId,
        totalObjectsLearned = totalObjectsLearned,
        averageQuizScore = averageQuizScore,
        favoriteCategories = favoriteCategories,
        weakAreas = weakAreas,
        learningLevel = learningLevel,
        learningStreak = learningStreak,
        totalLearningTimeMillis = totalLearningTimeMillis,
        frequentlySearchedTopics = frequentlySearchedTopics,
        difficultConcepts = difficultConcepts,
        preferredLanguage = preferredLanguage,
        learningSpeed = learningSpeed,
        personalizationEnabled = personalizationEnabled,
        updatedAt = updatedAt,
    )
}

fun LearningProfile.toEntity(isSynced: Boolean = false): LearningProfileEntity = LearningProfileEntity(
    userId = userId,
    totalObjectsLearned = totalObjectsLearned,
    averageQuizScore = averageQuizScore,
    favoriteCategories = favoriteCategories,
    weakAreas = weakAreas,
    learningLevel = learningLevel,
    learningStreak = learningStreak,
    totalLearningTimeMillis = totalLearningTimeMillis,
    frequentlySearchedTopics = frequentlySearchedTopics,
    difficultConcepts = difficultConcepts,
    preferredLanguage = preferredLanguage,
    learningSpeed = learningSpeed,
    personalizationEnabled = personalizationEnabled,
    isSynced = isSynced,
    updatedAt = updatedAt,
)
