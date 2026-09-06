package com.rola.app.global_education_network.network_core

import javax.inject.Inject

class GlobalEducationIdentityManager @Inject constructor() {
    fun identify(request: GlobalEducationNetworkRequest): GlobalEducationIdentity =
        GlobalEducationIdentity(
            identityId = "global-user-${request.userId}",
            role = GlobalParticipantRole.Learner,
            globalProfile = "${request.region} learner with ${request.goals.joinToString()} goals",
            skillRecognition = request.skills,
            learningHistory = request.learningHistory,
            verified = true,
        )
}
