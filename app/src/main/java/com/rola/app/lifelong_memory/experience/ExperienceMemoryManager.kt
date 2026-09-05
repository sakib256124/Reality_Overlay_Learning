package com.rola.app.lifelong_memory.experience

import com.rola.app.lifelong_memory.memory_core.LearningExperienceState
import com.rola.app.lifelong_memory.memory_core.LifelongMemoryContext
import javax.inject.Inject

class ExperienceMemoryManager @Inject constructor() {
    fun capture(context: LifelongMemoryContext): LearningExperienceState =
        LearningExperienceState(
            experienceId = "experience-${context.userId}",
            projects = context.completedProjects,
            experiments = listOf("AR lab replay", "digital twin exploration", "spatial learning practice"),
            researchWork = listOf("AI Research Assistant notes for ${context.currentLesson}"),
            practicalSkills = listOf("problem solving", "model building", "research recall"),
        )
}
