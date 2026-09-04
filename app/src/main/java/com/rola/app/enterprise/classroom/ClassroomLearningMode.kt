package com.rola.app.enterprise.classroom

import com.rola.app.data.database.EnterpriseDao
import com.rola.app.data.database.entities.toEntity
import com.rola.app.domain.model.ClassroomSession
import com.rola.app.domain.model.ClassroomSessionStatus
import com.rola.app.domain.model.Permission
import com.rola.app.enterprise.administration.RoleManager
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClassroomLearningMode @Inject constructor(
    private val enterpriseDao: EnterpriseDao,
    private val roleManager: RoleManager,
) {
    suspend fun startSession(
        institutionId: String,
        teacherId: String,
        classId: String,
        title: String,
        sharedObjectIds: List<String>,
    ): ClassroomSession {
        require(roleManager.hasPermission(teacherId, institutionId, Permission.ManageClasses)) {
            "Teacher class permission is required."
        }
        val session = ClassroomSession(
            sessionId = "classroom-session-${UUID.randomUUID()}",
            classId = classId,
            teacherId = teacherId,
            title = title,
            sharedObjectIds = sharedObjectIds.take(20),
            status = ClassroomSessionStatus.Live,
            startedAt = System.currentTimeMillis(),
        )
        enterpriseDao.upsertSessions(listOf(session.toEntity()))
        roleManager.audit(institutionId, teacherId, "start_classroom_session", "classroom_sessions", session.sessionId)
        return session
    }

    suspend fun joinSession(
        institutionId: String,
        studentId: String,
        session: ClassroomSession,
    ): ClassroomSession {
        require(roleManager.hasPermission(studentId, institutionId, Permission.JoinClassroomSession)) {
            "Student classroom session permission is required."
        }
        val updated = session.copy(participantIds = (session.participantIds + studentId).distinct())
        enterpriseDao.upsertSessions(listOf(updated.toEntity()))
        roleManager.audit(institutionId, studentId, "join_classroom_session", "classroom_sessions", session.sessionId)
        return updated
    }

    suspend fun completeSession(
        institutionId: String,
        teacherId: String,
        session: ClassroomSession,
    ): ClassroomSession {
        val updated = session.copy(status = ClassroomSessionStatus.Completed, endedAt = System.currentTimeMillis())
        enterpriseDao.upsertSessions(listOf(updated.toEntity()))
        roleManager.audit(institutionId, teacherId, "complete_classroom_session", "classroom_sessions", session.sessionId)
        return updated
    }
}
