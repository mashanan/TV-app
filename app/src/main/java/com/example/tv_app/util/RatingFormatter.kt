package com.example.tv_app.util

import java.util.Locale

fun formatRating(average: Double?): String {
    return average?.let { String.format(Locale.US, "%.1f", it) } ?: "N/A"
}
