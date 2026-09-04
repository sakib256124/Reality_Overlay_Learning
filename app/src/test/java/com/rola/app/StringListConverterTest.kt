package com.rola.app

import com.rola.app.data.local.database.StringListConverter
import org.junit.Assert.assertEquals
import org.junit.Test

class StringListConverterTest {
    private val converter = StringListConverter()

    @Test
    fun convertsListToPersistedStringAndBack() {
        val values = listOf("Food", "Medicine", "Shelter")

        val persisted = converter.fromList(values)
        val restored = converter.toList(persisted)

        assertEquals(values, restored)
    }
}
