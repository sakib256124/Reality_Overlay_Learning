package com.rola.app.knowledge_engineering.reasoning

import com.rola.app.knowledge_engineering.knowledge_core.KnowledgeDelivery
import javax.inject.Inject

class KnowledgeDeliveryEngine @Inject constructor() {
    fun refine(delivery: KnowledgeDelivery): KnowledgeDelivery = delivery.copy(resources = delivery.resources + "personalized resource")
}
