package com.rola.app.data.adaptive

import com.rola.app.domain.model.ChatMessage
import com.rola.app.domain.model.ChatRole
import com.rola.app.domain.model.LearningPattern
import com.rola.app.domain.model.LearningProfile
import com.rola.app.domain.model.LearningSpeed
import com.rola.app.domain.model.QuizResult
import com.rola.app.domain.model.ScanHistory
import com.rola.app.domain.model.SkillLevel
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

@Singleton
class LearningAnalyzer @Inject constructor() {
    fun buildProfile(
        userId: String,
        scans: List<ScanHistory>,
        quizResults: List<QuizResult>,
        messages: List<ChatMessage>,
        preferredLanguage: String,
        personalizationEnabled: Boolean = true,
    ): LearningProfile {
        val averageQuizScore = quizResults.map { it.percentage }.averageOrZero().roundToInt()
        val favoriteCategories = scans
            .groupingBy { it.category.ifBlank { "General" } }
            .eachCount()
            .entries
            .sortedWith(compareByDescending<Map.Entry<String, Int>> { it.value }.thenBy { it.key })
            .take(3)
            .map { it.key }
        val weakAreas = weakAreas(scans, quizResults, messages)
        val learningStreak = calculateStreak(scans.map { it.timestamp } + quizResults.map { it.timestamp })
        val learningSpeed = learningSpeed(scans, quizResults)

        return LearningProfile(
            userId = userId,
            totalObjectsLearned = scans.map { it.objectId }.distinct().size,
            averageQuizScore = averageQuizScore,
            favoriteCategories = favoriteCategories,
            weakAreas = weakAreas,
            learningLevel = skillLevel(averageQuizScore, scans, quizResults),
            learningStreak = learningStreak,
            totalLearningTimeMillis = estimateLearningTime(scans, quizResults, messages),
            frequentlySearchedTopics = frequentTopics(messages),
            difficultConcepts = difficultConcepts(messages, weakAreas),
            preferredLanguage = preferredLanguage,
            learningSpeed = learningSpeed,
            personalizationEnabled = personalizationEnabled,
            updatedAt = System.currentTimeMillis(),
        )
    }

    fun detectPattern(
        scans: List<ScanHistory>,
        quizResults: List<QuizResult>,
        messages: List<ChatMessage>,
    ): LearningPattern {
        val categoryCounts = scans.groupingBy { it.category.ifBlank { "General" } }.eachCount()
        val strongTopics = categoryCounts.entries
            .sortedByDescending { it.value }
            .take(3)
            .map { it.key }
        val weakTopics = weakAreas(scans, quizResults, messages)
        val recent = quizResults.sortedBy { it.timestamp }
        val trend = if (recent.size >= 2) {
            recent.takeLast(2).last().percentage - recent.takeLast(2).first().percentage
        } else {
            0
        }

        return LearningPattern(
            strongTopics = strongTopics,
            weakTopics = weakTopics,
            knowledgeGaps = knowledgeGaps(strongTopics, weakTopics, scans),
            preferredCategories = strongTopics,
            dailyLearningTimeMillis = estimateDailyLearningTime(scans, quizResults, messages),
            weeklyProgressPercent = weeklyProgress(scans, quizResults),
            quizImprovementTrend = trend,
            consistencyScore = consistencyScore(scans, quizResults),
            learningSpeed = learningSpeed(scans, quizResults),
        )
    }

    private fun weakAreas(
        scans: List<ScanHistory>,
        quizResults: List<QuizResult>,
        messages: List<ChatMessage>,
    ): List<String> = buildList {
        val average = quizResults.map { it.percentage }.averageOrZero()
        if (average in 1.0..59.0) add("Foundational concepts")
        if (quizResults.any { it.percentage < 50 }) add("Quiz accuracy")
        if (scans.any { it.confidenceScore < 0.55f }) add("Object identification confidence")
        addAll(
            messages
                .filter { it.role == ChatRole.User }
                .map { it.content.lowercase() }
                .filter { text -> difficultKeywords.any { it in text } }
                .mapNotNull { text -> difficultKeywords.firstOrNull { it in text } }
                .distinct()
                .map(::titleCase),
        )
    }.distinct().take(4)

    private fun skillLevel(
        averageQuizScore: Int,
        scans: List<ScanHistory>,
        quizResults: List<QuizResult>,
    ): SkillLevel = when {
        averageQuizScore >= 85 && scans.map { it.objectId }.distinct().size >= 20 && quizResults.size >= 5 -> SkillLevel.Advanced
        averageQuizScore >= 65 || scans.map { it.objectId }.distinct().size >= 8 -> SkillLevel.Intermediate
        else -> SkillLevel.Beginner
    }

    private fun learningSpeed(
        scans: List<ScanHistory>,
        quizResults: List<QuizResult>,
    ): LearningSpeed {
        val activeDays = (scans.map { it.timestamp } + quizResults.map { it.timestamp })
            .map { it / MILLIS_PER_DAY }
            .toSet()
            .size
            .coerceAtLeast(1)
        val eventsPerDay = (scans.size + quizResults.size).toFloat() / activeDays
        return when {
            eventsPerDay >= 6f -> LearningSpeed.Fast
            eventsPerDay <= 2f -> LearningSpeed.SlowAndSteady
            else -> LearningSpeed.Balanced
        }
    }

    private fun frequentTopics(messages: List<ChatMessage>): List<String> =
        messages
            .filter { it.role == ChatRole.User }
            .flatMap { message -> topicRegex.findAll(message.content.lowercase()).map { it.value } }
            .filter { it.length >= 4 && it !in stopWords }
            .groupingBy { it }
            .eachCount()
            .entries
            .sortedByDescending { it.value }
            .take(5)
            .map { titleCase(it.key) }

    private fun titleCase(value: String): String =
        value.replaceFirstChar { char -> char.uppercase() }

    private fun difficultConcepts(
        messages: List<ChatMessage>,
        weakAreas: List<String>,
    ): List<String> = (weakAreas + frequentTopics(messages).filter { topic ->
        difficultKeywords.any { keyword -> keyword in topic.lowercase() }
    }).distinct().take(5)

    private fun knowledgeGaps(
        strongTopics: List<String>,
        weakTopics: List<String>,
        scans: List<ScanHistory>,
    ): List<String> {
        val known = scans.map { it.category }.toSet()
        return buildList {
            if ("Plant" in strongTopics.joinToString()) add("Plant anatomy")
            if ("Plastic Object" in known || "Materials" in strongTopics.joinToString()) add("Material properties")
            if (weakTopics.isNotEmpty()) addAll(weakTopics)
            if (isEmpty()) add("Scientific classification")
        }.distinct().take(4)
    }

    private fun calculateStreak(timestamps: List<Long>): Int {
        val days = timestamps.map { it / MILLIS_PER_DAY }.toSet()
        var cursor = System.currentTimeMillis() / MILLIS_PER_DAY
        var streak = 0
        while (cursor in days) {
            streak++
            cursor--
        }
        return streak
    }

    private fun estimateLearningTime(
        scans: List<ScanHistory>,
        quizResults: List<QuizResult>,
        messages: List<ChatMessage>,
    ): Long = scans.size * SCAN_LEARNING_TIME_MILLIS +
        quizResults.sumOf { it.completionTime.coerceAtMost(MAX_QUIZ_TIME_MILLIS) } +
        messages.size * CHAT_LEARNING_TIME_MILLIS

    private fun estimateDailyLearningTime(
        scans: List<ScanHistory>,
        quizResults: List<QuizResult>,
        messages: List<ChatMessage>,
    ): Long {
        val activeDays = (scans.map { it.timestamp } + quizResults.map { it.timestamp } + messages.map { it.timestamp })
            .map { it / MILLIS_PER_DAY }
            .toSet()
            .size
            .coerceAtLeast(1)
        return estimateLearningTime(scans, quizResults, messages) / activeDays
    }

    private fun weeklyProgress(scans: List<ScanHistory>, quizResults: List<QuizResult>): Int {
        val cutoff = System.currentTimeMillis() - MILLIS_PER_DAY * 7
        val weeklyEvents = scans.count { it.timestamp >= cutoff } + quizResults.count { it.timestamp >= cutoff }
        return (weeklyEvents * 8).coerceIn(0, 100)
    }

    private fun consistencyScore(scans: List<ScanHistory>, quizResults: List<QuizResult>): Int {
        val activeDays = (scans.map { it.timestamp } + quizResults.map { it.timestamp })
            .map { it / MILLIS_PER_DAY }
            .toSet()
            .size
        return (activeDays * 14).coerceIn(0, 100)
    }

    private fun List<Int>.averageOrZero(): Double =
        if (isEmpty()) 0.0 else average()

    private companion object {
        const val MILLIS_PER_DAY = 86_400_000L
        const val SCAN_LEARNING_TIME_MILLIS = 90_000L
        const val CHAT_LEARNING_TIME_MILLIS = 45_000L
        const val MAX_QUIZ_TIME_MILLIS = 30 * 60_000L
        val topicRegex = Regex("[a-zA-Z]{4,}")
        val stopWords = setOf("what", "this", "that", "with", "from", "about", "does", "work", "give", "explain")
        val difficultKeywords = listOf("hard", "difficult", "confusing", "physics", "chemical", "material", "scientific")
    }
}
