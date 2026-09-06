package com.rola.app.virtual_campus_ai.collaboration

import com.rola.app.virtual_campus_ai.campus_core.CampusCollaborationPlan
import javax.inject.Inject

class VirtualCollaborationManager @Inject constructor() {
    fun improveQuality(plan: CampusCollaborationPlan): CampusCollaborationPlan =
        plan.copy(collaborativeTasks = plan.collaborativeTasks + "real-time peer feedback")
}
