package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.EducationEconomyAnalyticsEntity
import com.rola.app.data.database.entities.EducationEconomyCreatorProfileEntity
import com.rola.app.data.database.entities.EducationEconomyDigitalLearningAssetEntity
import com.rola.app.data.database.entities.EducationEconomyInnovationEntity
import com.rola.app.data.database.entities.EducationEconomyLearningValueScoreEntity
import com.rola.app.data.database.entities.EducationEconomyReputationRecordEntity
import com.rola.app.data.database.entities.EducationEconomySkillCertificateEntity
import com.rola.app.data.database.entities.EducationEconomyTransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EducationEconomyAIDao {
    @Query("SELECT * FROM education_economy_digital_learning_assets ORDER BY rowid DESC LIMIT 1") fun observeAsset(): Flow<EducationEconomyDigitalLearningAssetEntity?>
    @Query("SELECT * FROM education_economy_creator_profiles ORDER BY rowid DESC LIMIT 1") fun observeCreator(): Flow<EducationEconomyCreatorProfileEntity?>
    @Query("SELECT * FROM education_economy_innovations ORDER BY rowid DESC LIMIT 1") fun observeInnovation(): Flow<EducationEconomyInnovationEntity?>
    @Query("SELECT * FROM education_economy_skill_certificates ORDER BY rowid DESC LIMIT 1") fun observeCertificate(): Flow<EducationEconomySkillCertificateEntity?>
    @Query("SELECT * FROM education_economy_learning_value_scores ORDER BY rowid DESC LIMIT 1") fun observeValue(): Flow<EducationEconomyLearningValueScoreEntity?>
    @Query("SELECT * FROM education_economy_transactions ORDER BY rowid DESC LIMIT 1") fun observeTransaction(): Flow<EducationEconomyTransactionEntity?>
    @Query("SELECT * FROM education_economy_reputation_records ORDER BY rowid DESC LIMIT 1") fun observeReputation(): Flow<EducationEconomyReputationRecordEntity?>
    @Query("SELECT * FROM education_economy_analytics ORDER BY rowid DESC LIMIT 1") fun observeAnalytics(): Flow<EducationEconomyAnalyticsEntity?>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAsset(value: EducationEconomyDigitalLearningAssetEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertCreator(value: EducationEconomyCreatorProfileEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertInnovation(value: EducationEconomyInnovationEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertCertificate(value: EducationEconomySkillCertificateEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertValue(value: EducationEconomyLearningValueScoreEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertTransaction(value: EducationEconomyTransactionEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertReputation(value: EducationEconomyReputationRecordEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAnalytics(value: EducationEconomyAnalyticsEntity)
}
