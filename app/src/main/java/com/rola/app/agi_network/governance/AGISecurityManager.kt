package com.rola.app.agi_network.governance

import com.rola.app.agi_network.intelligence.AGINetworkAccessContext
import com.rola.app.agi_network.intelligence.AGINetworkAgentRole
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AGISecurityManager @Inject constructor() {
    fun agentAuthToken(
        role: AGINetworkAgentRole,
        accessContext: AGINetworkAccessContext,
    ): String = sha256("${accessContext.institutionId}:${accessContext.userId}:${role.name}").take(32)

    fun secureChannelId(
        source: AGINetworkAgentRole,
        target: AGINetworkAgentRole,
        institutionId: String,
    ): String = sha256("$institutionId:${source.name}->${target.name}").take(24)

    private fun sha256(value: String): String =
        MessageDigest.getInstance("SHA-256")
            .digest(value.toByteArray())
            .joinToString("") { "%02x".format(it) }
}
