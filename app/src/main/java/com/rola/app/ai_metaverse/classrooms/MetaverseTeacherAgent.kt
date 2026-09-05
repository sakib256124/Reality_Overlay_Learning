package com.rola.app.ai_metaverse.classrooms

import com.rola.app.ai_metaverse.intelligence.LearningAvatar
import com.rola.app.ai_metaverse.intelligence.MetaverseTeacherAction
import com.rola.app.ai_metaverse.intelligence.VirtualClassroomSession
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MetaverseTeacherAgent @Inject constructor() {
    fun teach(
        avatar: LearningAvatar,
        classroom: VirtualClassroomSession,
        question: String = "",
    ): MetaverseTeacherAction =
        MetaverseTeacherAction(
            actionId = "metaverse-teacher-action-${UUID.randomUUID()}",
            teacherAgentId = "ai-metaverse-teacher",
            topic = classroom.title,
            message = if (question.isBlank()) {
                "Welcome ${avatar.displayName}. Explore the shared object, observe evidence, and explain the concept in your own words."
            } else {
                "Let's answer by connecting the virtual object, your prior history, and the classroom evidence: $question"
            },
            demonstration = "Manipulate ${classroom.sharedObjects.firstOrNull() ?: "the main 3D object"} and run a guided virtual experiment.",
            adaptedMethod = if (avatar.knowledgeLevel.contains("beginner", ignoreCase = true)) "scaffolded demonstration" else "inquiry-based exploration",
        )
}

