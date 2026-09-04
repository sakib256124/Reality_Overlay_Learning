package com.rola.app.global.institutions

import com.rola.app.domain.model.GlobalInstitution
import com.rola.app.domain.model.InstitutionConnection
import com.rola.app.domain.model.VerificationRecord
import com.rola.app.domain.model.VerificationStatus
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobalInstitutionNetwork @Inject constructor() {
    fun registerInstitution(institution: GlobalInstitution): GlobalInstitution =
        institution.copy(verificationStatus = VerificationStatus.Pending, publicDirectory = false)

    fun verifyInstitution(
        institution: GlobalInstitution,
        reviewerId: String,
        approved: Boolean,
        notes: String,
    ): Pair<GlobalInstitution, VerificationRecord> {
        val status = if (approved) VerificationStatus.Verified else VerificationStatus.Rejected
        return institution.copy(verificationStatus = status, publicDirectory = approved) to VerificationRecord(
            recordId = "verification-${UUID.randomUUID()}",
            institutionId = institution.institutionId,
            reviewerId = reviewerId,
            status = status,
            notes = notes.take(500),
        )
    }

    fun directory(
        institutions: List<GlobalInstitution>,
        countryCode: String? = null,
        languageCode: String? = null,
    ): List<GlobalInstitution> = institutions
        .filter { it.verificationStatus == VerificationStatus.Verified && it.publicDirectory }
        .filter { countryCode == null || it.countryCode.equals(countryCode, ignoreCase = true) }
        .filter { languageCode == null || it.primaryLanguage.equals(languageCode, ignoreCase = true) }
        .sortedBy { it.name }

    fun activeConnections(
        institutionId: String,
        connections: List<InstitutionConnection>,
    ): Set<String> = connections
        .filter { it.status == com.rola.app.domain.model.CollaborationStatus.Active }
        .flatMap { listOf(it.sourceInstitutionId, it.targetInstitutionId) }
        .filterNot { it == institutionId }
        .toSet()
}
