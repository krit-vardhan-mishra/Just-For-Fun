package com.just_for_fun.justforfun.data

import java.util.Date

data class Review(
    val id: Int,
    val username: String,
    val avatarResId: Int,
    val comment: String,
    val rating: Float,
    val date: Date,
    var likeCount: Int = 0,
    var isLiked: Boolean = false,
    var replies: MutableList<Reply> = mutableListOf()
)