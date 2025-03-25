package com.just_for_fun.justforfun.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val name: String,
    val email: String,
    val username: String,
    val password: String
) : Parcelable
