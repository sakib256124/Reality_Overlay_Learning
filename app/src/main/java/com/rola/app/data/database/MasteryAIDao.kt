package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.AssessmentResultEntity
import com.rola.app.data.database.entities.CompetencyScoreEntity
import com.rola.app.data.database.entities.ImprovementPlanEntity
import com.rola.app.data.database.entities.LearningGapEntity
import com.rola.app.data.database.entities.MasteryHistoryEntity
import com.rola.app.data.database.entities.ProjectEvaluationEntity
import com.rola.app.data.database.entities.SkillMasteryProfileEntity
import com.rola.app.data.database.entities.SkillProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MasteryAIDao {
    @Query("SELECT * FROM skill_mastery_profiles ORDER BY rowid DESC LIMIT 1") fun observeProfile(): Flow<SkillMasteryProfileEntity?>
    @Query("SELECT * FROM competency_scores ORDER BY rowid DESC LIMIT 1") fun observeCompetency(): Flow<CompetencyScoreEntity?>
    @Query("SELECT * FROM learning_gaps ORDER BY rowid DESC LIMIT 1") fun observeGaps(): Flow<LearningGapEntity?>
    @Query("SELECT * FROM skill_progress ORDER BY rowid DESC LIMIT 1") fun observeProgress(): Flow<SkillProgressEntity?>
    @Query("SELECT * FROM mastery_history ORDER BY rowid DESC LIMIT 1") fun observeHistory(): Flow<MasteryHistoryEntity?>
    @Query("SELECT * FROM assessment_results ORDER BY rowid DESC LIMIT 1") fun observeAssessment(): Flow<AssessmentResultEntity?>
    @Query("SELECT * FROM improvement_plans ORDER BY rowid DESC LIMIT 1") fun observeImprovement(): Flow<ImprovementPlanEntity?>
    @Query("SELECT * FROM project_evaluations ORDER BY rowid DESC LIMIT 1") fun observeProject(): Flow<ProjectEvaluationEntity?>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertProfile(value: SkillMasteryProfileEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertCompetency(value: CompetencyScoreEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertGaps(value: LearningGapEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertProgress(value: SkillProgressEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertHistory(value: MasteryHistoryEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAssessment(value: AssessmentResultEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertImprovement(value: ImprovementPlanEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertProject(value: ProjectEvaluationEntity)
}
