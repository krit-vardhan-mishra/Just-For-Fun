package com.just_for_fun.justforfun.data

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserLoginData(
    val name: String,
    val email: String,
    val username: String,
    val password: String,
    val profilePhoto: Uri?
) : Parcelable
