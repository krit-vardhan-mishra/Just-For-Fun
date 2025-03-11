package com.just_for_fun.justforfun.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movies(
    val posterUrl: Int,
    val title: String,
    val description: String,
    val rating: Float,
    val type: String,
    val director: String,
    val releaseYear: Int,
    val totalAwards: String
) : Parcelable
