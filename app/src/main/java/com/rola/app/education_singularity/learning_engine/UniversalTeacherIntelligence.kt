package com.rola.app.education_singularity.learning_engine

import com.rola.app.education_singularity.universal_intelligence.SingularityLevel
import javax.inject.Inject

class UniversalTeacherIntelligence @Inject constructor() {
    fun teach(topic: String, level: SingularityLevel): String = when (level) {
        SingularityLevel.Beginner -> "Explain $topic with simple examples and visual guidance."
        SingularityLevel.Intermediate -> "Connect $topic to prior knowledge and adaptive practice."
        SingularityLevel.Advanced -> "Use multi-domain reasoning and research references for $topic."
        SingularityLevel.Research -> "Frame $topic as a research problem with knowledge discovery tasks."
    }
}
