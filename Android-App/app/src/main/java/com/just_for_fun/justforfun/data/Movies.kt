package com.just_for_fun.justforfun.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movies(
    val posterUrl: String,
    val title: String,
    val description: String,
    val rating: Double,
    val type: String,
    val director: String,
    val releaseYear: Int,
    val totalAwards: String
) : Parcelable
