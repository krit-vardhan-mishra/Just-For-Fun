package com.just_for_fun.justforfun.data

data class UserDetails(
    val name: String,
    val email: String,
    val username: String,
    val password: String,
    val profilePhoto: String?,
    val coverPhoto: String?,
    val bio: String?
)
