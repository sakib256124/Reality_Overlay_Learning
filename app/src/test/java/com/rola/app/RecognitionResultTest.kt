package com.rola.app

import com.rola.app.domain.model.RecognitionResult
import org.junit.Assert.assertEquals
import org.junit.Test

class RecognitionResultTest {
    @Test
    fun confidencePercentIsClampedToDisplayRange() {
        val result = RecognitionResult(
            name = "Bottle",
            confidence = 0.945f,
            timestamp = 1_725_000_000_000,
        )

        assertEquals(94, result.confidencePercent)
    }
}
