package com.rola.app.knowledge_engineering.reasoning

import com.rola.app.knowledge_engineering.knowledge_core.KnowledgeReasoning
import com.rola.app.knowledge_engineering.knowledge_core.StructuredKnowledge
import javax.inject.Inject

class KnowledgeReasoningEngine @Inject constructor() {
    fun reason(question: String, knowledge: StructuredKnowledge): KnowledgeReasoning =
        KnowledgeReasoning("reason-${knowledge.entityId}", "Answer for: $question", knowledge.definitions.take(4), "Reasoning connects definitions, examples, and skills into an explainable learning answer.")
}
