package com.rola.app.spatial_ai

import com.rola.app.domain.model.SpatialSession
import com.rola.app.domain.model.SpatialSessionStatus
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpatialSessionManager @Inject constructor() {
    fun startSession(
        learnerId: String,
        worldId: String,
    ): SpatialSession = SpatialSession(
        sessionId = "spatial-session-${UUID.randomUUID()}",
        learnerId = learnerId,
        worldId = worldId,
        status = SpatialSessionStatus.Entered,
        progressPercent = 0,
    )

    fun advance(session: SpatialSession): SpatialSession {
        val nextStatus = when (session.status) {
            SpatialSessionStatus.Entered -> SpatialSessionStatus.TeacherIntroduction
            SpatialSessionStatus.TeacherIntroduction -> SpatialSessionStatus.LessonPresentation
            SpatialSessionStatus.LessonPresentation -> SpatialSessionStatus.ObjectInteraction
            SpatialSessionStatus.ObjectInteraction -> SpatialSessionStatus.Assessment
            SpatialSessionStatus.Assessment -> SpatialSessionStatus.ReportReady
            SpatialSessionStatus.ReportReady -> SpatialSessionStatus.ReportReady
        }
        return session.copy(
            status = nextStatus,
            progressPercent = progressFor(nextStatus),
            updatedAt = System.currentTimeMillis(),
        )
    }

    private fun progressFor(status: SpatialSessionStatus): Int = when (status) {
        SpatialSessionStatus.Entered -> 0
        SpatialSessionStatus.TeacherIntroduction -> 15
        SpatialSessionStatus.LessonPresentation -> 35
        SpatialSessionStatus.ObjectInteraction -> 65
        SpatialSessionStatus.Assessment -> 85
        SpatialSessionStatus.ReportReady -> 100
    }
}
