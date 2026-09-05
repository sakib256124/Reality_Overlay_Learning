package com.rola.app.knowledge_engineering.knowledge_core

import javax.inject.Inject

class KnowledgeCoreManager @Inject constructor() {
    fun deliver(request: KnowledgeEngineeringRequest, structured: StructuredKnowledge): KnowledgeDelivery =
        KnowledgeDelivery("delivery-${request.userId}", "${request.studentLevel} ${request.learningStyle} explanation for ${request.topic}", structured.learningMaterials + structured.examples, listOf(request.studentLevel, request.learningStyle, "cognitive profile", "emotional state"))
}
