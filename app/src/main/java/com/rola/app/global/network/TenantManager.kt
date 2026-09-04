package com.rola.app.global.network

import com.rola.app.domain.model.SharedKnowledgeResource
import com.rola.app.domain.model.TenantScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TenantManager @Inject constructor() {
    fun canAccessResource(
        tenant: TenantScope,
        resource: SharedKnowledgeResource,
        connectedInstitutionIds: Set<String>,
    ): Boolean = when (resource.visibility) {
        com.rola.app.domain.model.ResourceVisibility.Private -> tenant.owns(resource.ownerInstitutionId)
        com.rola.app.domain.model.ResourceVisibility.ConnectedInstitutions ->
            tenant.owns(resource.ownerInstitutionId) || resource.ownerInstitutionId in connectedInstitutionIds
        com.rola.app.domain.model.ResourceVisibility.Public,
        com.rola.app.domain.model.ResourceVisibility.Marketplace,
        -> true
    }

    fun scopedCollectionPath(
        tenant: TenantScope,
        collection: String,
    ): String = "tenants/${tenant.institutionId}/$collection"
}
