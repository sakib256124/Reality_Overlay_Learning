package com.rola.app.knowledge_engineering.processing

import com.rola.app.knowledge_engineering.knowledge_core.ExtractedKnowledge
import com.rola.app.knowledge_engineering.knowledge_core.StructuredKnowledge
import javax.inject.Inject

class KnowledgeProcessingManager @Inject constructor() {
    fun process(extracted: ExtractedKnowledge): StructuredKnowledge =
        StructuredKnowledge(
            entityId = "structured-${extracted.extractionId}",
            definitions = extracted.concepts.map { "$it definition" },
            learningMaterials = extracted.importantFacts,
            examples = extracted.concepts.take(3).map { "$it example" },
            skills = listOf("understand concepts", "connect relationships", "apply knowledge"),
        )
}
