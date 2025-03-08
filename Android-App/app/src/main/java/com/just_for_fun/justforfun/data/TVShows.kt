package com.just_for_fun.justforfun.data

data class TVShows(
    val name: String,
    val seasons: Int,
    val episodes: Int,
    val yearStarting: Int,
    val yearEnding: Int,
    val totalRating: Float,
    val cast: List<CastCrewMember>,
    val movieDescription: String,
    val poster: Int,
    val moreLikeThis: List<Movies>
)
