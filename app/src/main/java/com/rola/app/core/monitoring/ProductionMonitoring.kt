package com.rola.app.core.monitoring

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import com.rola.app.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductionMonitoring @Inject constructor(
    private val analytics: FirebaseAnalytics,
    private val crashlytics: FirebaseCrashlytics,
    private val performance: FirebasePerformance,
) {
    fun configure(userId: String?, analyticsEnabled: Boolean = true) {
        analytics.setAnalyticsCollectionEnabled(analyticsEnabled)
        crashlytics.setCrashlyticsCollectionEnabled(BuildConfig.CRASHLYTICS_ENABLED)
        performance.isPerformanceCollectionEnabled = analyticsEnabled
        if (!userId.isNullOrBlank()) {
            analytics.setUserId(userId)
            crashlytics.setUserId(userId)
        }
        crashlytics.setCustomKey("environment", BuildConfig.ENVIRONMENT)
    }

    fun recordLearningSession(
        sessionId: String,
        objective: String,
        completedSteps: Int,
    ) {
        analytics.logEvent(
            "learning_session_complete",
            Bundle().apply {
                putString("session_id", sessionId)
                putString("objective", objective.take(MAX_PARAM_LENGTH))
                putInt("completed_steps", completedSteps)
                putString("environment", BuildConfig.ENVIRONMENT)
            },
        )
    }

    fun recordAiLatency(
        operation: String,
        latencyMillis: Long,
        success: Boolean,
    ) {
        analytics.logEvent(
            "ai_latency",
            Bundle().apply {
                putString("operation", operation.take(MAX_PARAM_LENGTH))
                putLong("latency_ms", latencyMillis)
                putString("success", success.toString())
            },
        )
    }

    fun recordNonFatal(
        throwable: Throwable,
        area: String,
    ) {
        crashlytics.setCustomKey("area", area.take(MAX_PARAM_LENGTH))
        crashlytics.recordException(throwable)
    }

    fun startTrace(name: String): Trace =
        performance.newTrace(name.take(MAX_TRACE_NAME_LENGTH)).also { it.start() }

    fun stopTrace(
        trace: Trace,
        metrics: Map<String, Long> = emptyMap(),
    ) {
        metrics.forEach { (name, value) -> trace.putMetric(name.take(MAX_TRACE_NAME_LENGTH), value) }
        trace.stop()
    }

    private companion object {
        const val MAX_PARAM_LENGTH = 100
        const val MAX_TRACE_NAME_LENGTH = 100
    }
}
