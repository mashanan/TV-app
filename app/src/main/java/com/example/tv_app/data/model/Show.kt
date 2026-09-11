package com.example.tv_app.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Show(
    val id: Int,
    val url: String? = null,
    val name: String,
    val summary: String? = null,
    val premiered: String? = null,
    val rating: Rating? = null,
    val image: ShowImage? = null,
    @SerialName("_embedded") val embedded: Embedded? = null
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

@Serializable
data class Embedded(
    val episodes: List<Episode>? = null,
    val cast: List<CastCredit>? = null
)

@Serializable
data class Episode(
    val id: Int,
    val name: String,
    val season: Int,
    val number: Int? = null
)

@Serializable
data class CastCredit(
    val person: Person,
    val character: Character
)

@Serializable
data class Person(
    val id: Int,
    val name: String,
    val image: ShowImage? = null
)

@Serializable
data class Character(
    val id: Int,
    val name: String
)
