package com.rola.app.digital_companion.privacy

import com.rola.app.digital_companion.companion_core.CompanionPrivacyState
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanionPrivacyManager @Inject constructor() {
    fun privacyState(
        memoryEnabled: Boolean = true,
        cloudSyncEnabled: Boolean = false,
    ): CompanionPrivacyState =
        CompanionPrivacyState(
            privacyId = "companion-privacy-${UUID.randomUUID()}",
            memoryEnabled = memoryEnabled,
            cloudSyncEnabled = cloudSyncEnabled,
            deletionControlAvailable = true,
            transparentDecisions = true,
        )
}

