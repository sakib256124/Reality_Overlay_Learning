package com.rola.app.lifelong_memory.intelligence

import com.rola.app.lifelong_memory.memory_core.ExpertiseLevel
import com.rola.app.lifelong_memory.memory_core.LearningExperienceState
import com.rola.app.lifelong_memory.memory_core.LifelongMemoryContext
import com.rola.app.lifelong_memory.memory_core.PersonalKnowledgeGraphState
import javax.inject.Inject

class PersonalKnowledgeGraph @Inject constructor() {
    fun build(context: LifelongMemoryContext, experience: LearningExperienceState): PersonalKnowledgeGraphState =
        PersonalKnowledgeGraphState(
            graphId = "personal-graph-${context.userId}",
            concepts = listOf(context.currentLesson, context.activeGoal),
            skills = experience.practicalSkills,
            experiences = experience.projects + experience.experiments,
            achievements = context.achievements,
            expertise = if (context.achievements.size >= 3) ExpertiseLevel.Advanced else ExpertiseLevel.Intermediate,
        )
}
