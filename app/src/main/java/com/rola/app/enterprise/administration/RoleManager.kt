package com.rola.app.enterprise.administration

import com.rola.app.data.database.EnterpriseDao
import com.rola.app.data.database.entities.toEntity
import com.rola.app.domain.model.EnterpriseAuditLog
import com.rola.app.domain.model.EnterpriseUserRole
import com.rola.app.domain.model.Permission
import com.rola.app.domain.model.UserRole
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

@Singleton
class RoleManager @Inject constructor(
    private val enterpriseDao: EnterpriseDao,
) {
    fun observeRoles(userId: String): Flow<List<EnterpriseUserRole>> =
        enterpriseDao.observeRoles(userId).map { roles -> roles.map { it.toDomain() } }

    suspend fun assignRole(
        actorUserId: String,
        role: EnterpriseUserRole,
    ) {
        if (role.role != UserRole.Student && !hasPermission(actorUserId, role.institutionId, Permission.ManageInstitution)) {
            throw SecurityException("Institution management permission is required.")
        }
        enterpriseDao.upsertRoles(listOf(role.toEntity()))
        audit(role.institutionId, actorUserId, "assign_role", "enterprise_roles", role.userId)
    }

    suspend fun hasPermission(
        userId: String,
        institutionId: String,
        permission: Permission,
    ): Boolean {
        val roles = enterpriseDao.observeRoles(userId).first().map { it.toDomain() }
        return roles.any { role ->
            role.active && role.institutionId == institutionId && permission in role.permissions
        }
    }

    fun isFeatureAvailable(
        roles: List<EnterpriseUserRole>,
        permission: Permission,
    ): Boolean = roles.any { it.active && permission in it.permissions }

    suspend fun audit(
        institutionId: String,
        actorUserId: String,
        action: String,
        targetType: String,
        targetId: String,
    ) {
        enterpriseDao.insertAuditLogs(
            listOf(
                EnterpriseAuditLog(
                    auditId = "audit-${UUID.randomUUID()}",
                    institutionId = institutionId,
                    actorUserId = actorUserId,
                    action = action,
                    targetType = targetType,
                    targetId = targetId,
                ).toEntity(),
            ),
        )
    }
}
