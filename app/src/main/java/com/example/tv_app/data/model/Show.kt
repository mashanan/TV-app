package com.example.tv_app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Show(
    val id: Int,
    val url: String? = null,
    val name: String,
    val summary: String? = null,
    val premiered: String? = null,
    val rating: Rating? = null,
    val image: ShowImage? = null
)

@Serializable
data class ShowImage(
    val medium: String? = null,
    val original: String? = null
)

@Serializable
data class Rating(
    val average: Double? = null
)
