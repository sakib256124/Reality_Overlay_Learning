package com.rola.app.enterprise.administration

import com.rola.app.data.database.EnterpriseDao
import com.rola.app.data.database.entities.toEntity
import com.rola.app.domain.model.LMSConnection
import com.rola.app.domain.model.LMSConnectionStatus
import com.rola.app.domain.model.LMSProvider
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class LMSConnector @Inject constructor(
    private val enterpriseDao: EnterpriseDao,
    private val roleManager: RoleManager,
) {
    fun observeConnections(institutionId: String): Flow<List<LMSConnection>> =
        enterpriseDao.observeLmsConnections(institutionId).map { rows -> rows.map { it.toDomain() } }

    suspend fun prepareConnection(
        actorUserId: String,
        institutionId: String,
        provider: LMSProvider,
    ): LMSConnection {
        require(roleManager.hasPermission(actorUserId, institutionId, com.rola.app.domain.model.Permission.ManageInstitution)) {
            "Institution administration permission is required for LMS setup."
        }
        val connection = LMSConnection(
            connectionId = "lms-${UUID.randomUUID()}",
            institutionId = institutionId,
            provider = provider,
            status = LMSConnectionStatus.NotConfigured,
            syncEnabled = false,
        )
        enterpriseDao.upsertLmsConnections(listOf(connection.toEntity()))
        roleManager.audit(institutionId, actorUserId, "prepare_lms_connection", "lms_connections", connection.connectionId)
        return connection
    }

    fun supportedProviders(): List<LMSProvider> = listOf(
        LMSProvider.Moodle,
        LMSProvider.GoogleClassroom,
        LMSProvider.MicrosoftTeams,
        LMSProvider.Canvas,
    )
}
