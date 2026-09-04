package com.rola.app.data.cloud

import com.rola.app.data.remote.api.ROLAApiService
import com.rola.app.domain.model.CloudAnalyticsEvent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CloudAnalyticsService @Inject constructor(
    private val apiService: ROLAApiService,
) {
    suspend fun recordEvent(
        eventType: String,
        metrics: Map<String, Any?> = emptyMap(),
    ) {
        runCatching {
            apiService.recordAnalytics(
                CloudAnalyticsEvent(
                    eventType = eventType.take(MAX_EVENT_TYPE_LENGTH),
                    metrics = metrics.sanitize(),
                ),
            )
        }
    }

    private fun Map<String, Any?>.sanitize(): Map<String, Any?> =
        entries
            .take(MAX_METRICS)
            .associate { (key, value) ->
                key.take(MAX_KEY_LENGTH) to when (value) {
                    is String -> value.take(MAX_VALUE_LENGTH)
                    is Number,
                    is Boolean,
                    null,
                    -> value
                    else -> value.toString().take(MAX_VALUE_LENGTH)
                }
            }

    private companion object {
        const val MAX_EVENT_TYPE_LENGTH = 80
        const val MAX_METRICS = 30
        const val MAX_KEY_LENGTH = 60
        const val MAX_VALUE_LENGTH = 500
    }
}
