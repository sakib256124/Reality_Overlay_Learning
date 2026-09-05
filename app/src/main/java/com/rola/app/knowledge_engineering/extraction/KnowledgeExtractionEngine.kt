package com.rola.app.knowledge_engineering.extraction

import com.rola.app.knowledge_engineering.knowledge_core.ExtractedKnowledge
import com.rola.app.knowledge_engineering.knowledge_core.KnowledgeEngineeringRequest
import javax.inject.Inject

class KnowledgeExtractionEngine @Inject constructor() {
    fun extract(request: KnowledgeEngineeringRequest): ExtractedKnowledge =
        ExtractedKnowledge(
            extractionId = "extract-${request.topic.lowercase().replace(" ", "-")}",
            concepts = listOf(request.topic) + request.rawKnowledge.map { it.take(32) },
            importantFacts = request.rawKnowledge,
            relationships = listOf("${request.topic} -> definition", "${request.topic} -> example", "${request.topic} -> skill"),
            classifications = listOf("concept", "learning material", "research information"),
        )
}
