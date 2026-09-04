package com.rola.app.neural_ai.performance

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter

@Singleton
class NeuralPerformanceOptimizer @Inject constructor() {
    fun <T> throttleSignals(
        source: Flow<T>,
        everyNthSignal: Int = DEFAULT_SIGNAL_SKIP,
    ): Flow<T> {
        var count = 0
        val safeSkip = everyNthSignal.coerceAtLeast(1)
        return source.filter {
            count += 1
            count % safeSkip == 0
        }
    }

    fun recommendedSamplingIntervalMillis(batteryPercent: Int, thermalLimited: Boolean): Long =
        when {
            thermalLimited -> 3_000L
            batteryPercent <= 20 -> 2_500L
            batteryPercent <= 45 -> 1_800L
            else -> 1_200L
        }

    companion object {
        const val DEFAULT_SIGNAL_SKIP = 2
    }
}
