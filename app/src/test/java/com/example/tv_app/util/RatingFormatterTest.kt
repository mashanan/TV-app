package com.example.tv_app.util

import org.junit.Assert.assertEquals
import org.junit.Test

class RatingFormatterTest {

    @Test
    fun `formatRating returns N-A when average is null`() {
        assertEquals("N/A", formatRating(null))
    }

    @Test
    fun `formatRating formats average with one decimal place`() {
        assertEquals("8.5", formatRating(8.5))
    }
}
