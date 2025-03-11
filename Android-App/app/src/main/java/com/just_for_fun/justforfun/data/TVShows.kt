package com.just_for_fun.justforfun.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TVShows(
    val posterUrl: Int,
    val title: String,
    val description: String,
    val rating: Float,
    val type: String,
    val totalSeasons: Int,
    val totalEpisodes: Int,
    val showRunner: String,
    val yearAired: String
) : Parcelable
