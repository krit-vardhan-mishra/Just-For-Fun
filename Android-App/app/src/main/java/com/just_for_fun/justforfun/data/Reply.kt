package com.just_for_fun.justforfun.data

import java.util.Date

data class Reply(
    val id: String,
    val username: String,
    val avatarResId: Int,
    val comment: String,
    val date: Date,
    var likedCount: Int = 0,
    var isLiked: Boolean = false
)