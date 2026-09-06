package com.rola.app.virtual_campus_ai.intelligence

import com.rola.app.virtual_campus_ai.campus_core.VirtualCampusRequest
import com.rola.app.virtual_campus_ai.campus_core.VirtualClassroomPlan
import com.rola.app.virtual_campus_ai.campus_core.VirtualTeacherSession
import javax.inject.Inject

class VirtualTeacherAgent @Inject constructor() {
    fun conductClass(request: VirtualCampusRequest, classroom: VirtualClassroomPlan): VirtualTeacherSession =
        VirtualTeacherSession(
            teacherSessionId = "teacher-session-${request.learnerId}",
            classesConducted = listOf(classroom.title, "Virtual Physics Lab"),
            conceptExplanations = listOf("explains ${request.courseTopic} through experiment", "answers student questions"),
            discussionPrompts = listOf("compare simulation results", "defend the project design"),
            learnerEvaluations = listOf("formative assessment", "collaboration quality review"),
        )
}
