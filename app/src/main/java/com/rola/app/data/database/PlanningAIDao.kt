package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.AdaptiveChangeEntity
import com.rola.app.data.database.entities.CareerRoadmapEntity
import com.rola.app.data.database.entities.LearningPlanEntity
import com.rola.app.data.database.entities.OptimizationHistoryEntity
import com.rola.app.data.database.entities.PlanningLearningGoalEntity
import com.rola.app.data.database.entities.ResearchPlanEntity
import com.rola.app.data.database.entities.ScheduleEntity
import com.rola.app.data.database.entities.StrategyRecordEntity
import com.rola.app.data.database.entities.TaskExecutionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanningAIDao {
    @Query("SELECT * FROM planning_learning_goals ORDER BY rowid DESC LIMIT 1") fun observeGoal(): Flow<PlanningLearningGoalEntity?>
    @Query("SELECT * FROM learning_plans ORDER BY rowid DESC LIMIT 1") fun observePlan(): Flow<LearningPlanEntity?>
    @Query("SELECT * FROM strategy_records ORDER BY rowid DESC LIMIT 1") fun observeStrategy(): Flow<StrategyRecordEntity?>
    @Query("SELECT * FROM schedules ORDER BY rowid DESC LIMIT 1") fun observeSchedule(): Flow<ScheduleEntity?>
    @Query("SELECT * FROM optimization_history ORDER BY rowid DESC LIMIT 1") fun observeOptimization(): Flow<OptimizationHistoryEntity?>
    @Query("SELECT * FROM adaptive_changes ORDER BY rowid DESC LIMIT 1") fun observeAdaptiveChange(): Flow<AdaptiveChangeEntity?>
    @Query("SELECT * FROM task_execution ORDER BY rowid DESC LIMIT 1") fun observeExecution(): Flow<TaskExecutionEntity?>
    @Query("SELECT * FROM career_roadmaps ORDER BY rowid DESC LIMIT 1") fun observeCareerRoadmap(): Flow<CareerRoadmapEntity?>
    @Query("SELECT * FROM research_plans ORDER BY rowid DESC LIMIT 1") fun observeResearchPlan(): Flow<ResearchPlanEntity?>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertGoal(value: PlanningLearningGoalEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertPlan(value: LearningPlanEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertStrategy(value: StrategyRecordEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertSchedule(value: ScheduleEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertOptimization(value: OptimizationHistoryEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAdaptiveChange(value: AdaptiveChangeEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertExecution(value: TaskExecutionEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertCareerRoadmap(value: CareerRoadmapEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertResearchPlan(value: ResearchPlanEntity)
}
