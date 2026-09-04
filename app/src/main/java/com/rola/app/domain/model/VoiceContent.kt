package com.rola.app.domain.model

data class VoiceContent(
    val objectId: String,
    val title: String,
    val description: String,
    val scientificInformation: String,
    val uses: List<String>,
    val facts: List<String>,
    val explanationDuration: Long,
) {
    val explanationText: String
        get() = listOf(
            "This is a $title.",
            description,
            "It belongs to $scientificInformation.",
            uses.takeIf { it.isNotEmpty() }?.joinToString(
                prefix = "It is commonly used for ",
                separator = ", ",
                postfix = ".",
            ).orEmpty(),
            facts.firstOrNull()?.let { fact -> "An interesting fact is $fact" }.orEmpty(),
        )
            .filter { it.isNotBlank() }
            .joinToString(separator = " ")
}

fun ObjectInformation.toVoiceContent(): VoiceContent {
    val text = listOf(
        name,
        description,
        scientificName,
        uses.joinToString(),
        facts.joinToString(),
    ).joinToString(separator = " ")

    return VoiceContent(
        objectId = objectId,
        title = name,
        description = description,
        scientificInformation = "$category. Scientific name: $scientificName",
        uses = uses,
        facts = facts,
        explanationDuration = estimateSpeechDurationMillis(text),
    )
}

private fun estimateSpeechDurationMillis(text: String): Long {
    val words = text.split(Regex("\\s+")).count { it.isNotBlank() }
    val duration = (words / DEFAULT_WORDS_PER_MINUTE * 60_000f).toLong()
    return duration.coerceAtLeast(3_000L)
}

private const val DEFAULT_WORDS_PER_MINUTE = 145f
