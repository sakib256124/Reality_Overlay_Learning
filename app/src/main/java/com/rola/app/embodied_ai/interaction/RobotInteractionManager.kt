package com.rola.app.embodied_ai.interaction

import com.rola.app.domain.model.RobotInputMode
import com.rola.app.domain.model.RobotInteraction
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RobotInteractionManager @Inject constructor() {
    fun respond(
        robotId: String,
        studentId: String,
        inputMode: RobotInputMode,
        inputText: String,
        emotionalSignal: String,
    ): RobotInteraction =
        RobotInteraction(
            interactionId = "robot-interaction-${UUID.randomUUID()}",
            robotId = robotId,
            studentId = studentId,
            inputMode = inputMode,
            inputText = inputText,
            responseText = responseFor(inputText, inputMode),
            emotionResponse = emotionResponseFor(emotionalSignal),
        )

    fun understandGesture(gesture: String): String = when {
        gesture.contains("raise", ignoreCase = true) -> "Student wants to ask a question."
        gesture.contains("point", ignoreCase = true) -> "Student is referencing a physical object."
        gesture.contains("thumb", ignoreCase = true) -> "Student is giving feedback."
        else -> "Gesture needs clarification."
    }

    private fun responseFor(
        inputText: String,
        inputMode: RobotInputMode,
    ): String =
        "I understood the ${inputMode.name.lowercase()} input: ${inputText.ifBlank { "continue lesson" }}."

    private fun emotionResponseFor(signal: String): String = when {
        signal.contains("confused", ignoreCase = true) -> "I'll slow down and use a simpler example."
        signal.contains("excited", ignoreCase = true) -> "Great energy. Let's try a challenge."
        signal.contains("quiet", ignoreCase = true) -> "I'll ask a gentle check-in question."
        else -> "I'll continue with supportive guidance."
    }
}
