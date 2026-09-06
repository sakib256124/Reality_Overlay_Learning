package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.EducationMarketplaceCourseModelEntity
import com.rola.app.data.database.entities.EducationMarketplaceCreatorEntity
import com.rola.app.data.database.entities.EducationMarketplaceLearningMaterialEntity
import com.rola.app.data.database.entities.EducationMarketplaceRecommendationEntity
import com.rola.app.data.database.entities.EducationMarketplaceResourceAnalyticsEntity
import com.rola.app.data.database.entities.EducationMarketplaceResourceEntity
import com.rola.app.data.database.entities.EducationMarketplaceResourceRatingEntity
import com.rola.app.data.database.entities.EducationMarketplaceTransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EducationMarketplaceAIDao {
    @Query("SELECT * FROM education_marketplace_resources ORDER BY rowid DESC LIMIT 1") fun observeResource(): Flow<EducationMarketplaceResourceEntity?>
    @Query("SELECT * FROM education_marketplace_creators ORDER BY rowid DESC LIMIT 1") fun observeCreator(): Flow<EducationMarketplaceCreatorEntity?>
    @Query("SELECT * FROM education_marketplace_resource_ratings ORDER BY rowid DESC LIMIT 1") fun observeRating(): Flow<EducationMarketplaceResourceRatingEntity?>
    @Query("SELECT * FROM education_marketplace_recommendations ORDER BY rowid DESC LIMIT 1") fun observeRecommendation(): Flow<EducationMarketplaceRecommendationEntity?>
    @Query("SELECT * FROM education_marketplace_course_models ORDER BY rowid DESC LIMIT 1") fun observeCourse(): Flow<EducationMarketplaceCourseModelEntity?>
    @Query("SELECT * FROM education_marketplace_learning_materials ORDER BY rowid DESC LIMIT 1") fun observeMaterial(): Flow<EducationMarketplaceLearningMaterialEntity?>
    @Query("SELECT * FROM education_marketplace_resource_analytics ORDER BY rowid DESC LIMIT 1") fun observeAnalytics(): Flow<EducationMarketplaceResourceAnalyticsEntity?>
    @Query("SELECT * FROM education_marketplace_transactions ORDER BY rowid DESC LIMIT 1") fun observeTransaction(): Flow<EducationMarketplaceTransactionEntity?>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertResource(value: EducationMarketplaceResourceEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertCreator(value: EducationMarketplaceCreatorEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertRating(value: EducationMarketplaceResourceRatingEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertRecommendation(value: EducationMarketplaceRecommendationEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertCourse(value: EducationMarketplaceCourseModelEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertMaterial(value: EducationMarketplaceLearningMaterialEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAnalytics(value: EducationMarketplaceResourceAnalyticsEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertTransaction(value: EducationMarketplaceTransactionEntity)
}
