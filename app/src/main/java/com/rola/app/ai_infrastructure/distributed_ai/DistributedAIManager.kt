package com.rola.app.ai_infrastructure.distributed_ai

import com.rola.app.ai_infrastructure.models.AIInfrastructureRequest
import com.rola.app.ai_infrastructure.models.DistributedAIWorkload
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DistributedAIManager @Inject constructor(
    private val distributedAIEngine: DistributedAIEngine,
) {
    fun coordinate(request: AIInfrastructureRequest): DistributedAIWorkload =
        distributedAIEngine.distribute(request)
}

