package com.just_for_fun.justforfun.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TVShows(
    val posterUrl: String,
    val title: String,
    val description: String,
    val rating: Double,
    val type: String,
    val totalSeasons: Int,
    val totalEpisodes: Int,
    val showRunner: String,
    val yearAired: String
) : Parcelable
