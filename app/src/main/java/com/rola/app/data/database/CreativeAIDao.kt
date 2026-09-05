package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.CreativeContentEntity
import com.rola.app.data.database.entities.CreativeEvaluationEntity
import com.rola.app.data.database.entities.CreativeGeneratedLessonEntity
import com.rola.app.data.database.entities.CreativeInnovationRecordEntity
import com.rola.app.data.database.entities.CreativeProjectEntity
import com.rola.app.data.database.entities.HumanAIProjectEntity
import com.rola.app.data.database.entities.ResearchIdeaEntity
import com.rola.app.data.database.entities.SimulationTemplateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CreativeAIDao {
    @Query("SELECT * FROM creative_contents ORDER BY rowid DESC LIMIT 1") fun observeContent(): Flow<CreativeContentEntity?>
    @Query("SELECT * FROM creative_innovation_records ORDER BY rowid DESC LIMIT 1") fun observeInnovation(): Flow<CreativeInnovationRecordEntity?>
    @Query("SELECT * FROM research_ideas ORDER BY rowid DESC LIMIT 1") fun observeResearch(): Flow<ResearchIdeaEntity?>
    @Query("SELECT * FROM creative_projects ORDER BY rowid DESC LIMIT 1") fun observeProject(): Flow<CreativeProjectEntity?>
    @Query("SELECT * FROM creative_evaluations ORDER BY rowid DESC LIMIT 1") fun observeEvaluation(): Flow<CreativeEvaluationEntity?>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertContent(value: CreativeContentEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertLesson(value: CreativeGeneratedLessonEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertInnovation(value: CreativeInnovationRecordEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertResearch(value: ResearchIdeaEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertProject(value: CreativeProjectEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertSimulation(value: SimulationTemplateEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertEvaluation(value: CreativeEvaluationEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertHumanProject(value: HumanAIProjectEntity)
}
