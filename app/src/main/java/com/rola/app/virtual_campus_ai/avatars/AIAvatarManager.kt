package com.rola.app.virtual_campus_ai.avatars

import com.rola.app.virtual_campus_ai.campus_core.CampusAvatar
import com.rola.app.virtual_campus_ai.campus_core.CampusAvatarRole
import com.rola.app.virtual_campus_ai.campus_core.VirtualCampusRequest
import javax.inject.Inject

class AIAvatarManager @Inject constructor() {
    fun createAvatars(request: VirtualCampusRequest): List<CampusAvatar> =
        listOf(
            CampusAvatar("teacher-${request.learnerId}", CampusAvatarRole.Teacher, true, "calm facial expression model", "clear Socratic explanation", "adapts to ${request.learningGoal}"),
            CampusAvatar("assistant-${request.learnerId}", CampusAvatarRole.StudentAssistant, true, "encouraging expression model", "step-by-step guidance", "remembers current course context"),
            CampusAvatar("research-${request.learnerId}", CampusAvatarRole.ResearchAssistant, true, "focused expression model", "evidence-oriented research support", "suggests verified resources"),
            CampusAvatar("mentor-${request.learnerId}", CampusAvatarRole.Mentor, true, "supportive expression model", "future roadmap coaching", "personalizes campus navigation"),
        )
}
