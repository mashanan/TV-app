package com.example.tv_app.util

import androidx.core.text.HtmlCompat

fun htmlToPlainText(html: String?): String {
    if (html.isNullOrBlank()) return ""
    return HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT).toString().trim()
}
