package com.just_for_fun.justforfun.data

data class Movies(
    val name: String,
    val totalRating: Float,
    val releaseYear: Int,
    val movieDescription: String,
    val poster: Int,
    val cast: List<CastCrewMember>,
    val moreLikeThis: List<Movies>
)
