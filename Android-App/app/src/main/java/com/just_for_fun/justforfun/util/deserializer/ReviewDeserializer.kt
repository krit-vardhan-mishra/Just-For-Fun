package com.just_for_fun.justforfun.util.deserializer

import android.app.Application
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.just_for_fun.justforfun.data.Reply
import com.just_for_fun.justforfun.data.Review
import java.lang.reflect.Type
import java.util.Date

class ReviewDeserializer(private val application: Application) : JsonDeserializer<Review> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: com.google.gson.JsonDeserializationContext
    ): Review {
        val jsonObject = json.asJsonObject

        val avatarResIdString = jsonObject.get("avatarResId").asString
        val resourceName = avatarResIdString.replace("R.drawable.", "")
        val avatarResId = application.resources.getIdentifier(
            resourceName, "drawable", application.packageName
        )

        return Review(
            id = jsonObject.get("id").asInt,
            username = jsonObject.get("username").asString,
            avatarResId = avatarResId,
            comment = jsonObject.get("comment").asString,
            rating = jsonObject.get("rating").asFloat,
            date = context.deserialize(jsonObject.get("date"), Date::class.java),
            likeCount = jsonObject.get("likeCount").asInt,
            isLiked = jsonObject.get("isLiked").asBoolean,
            replies = context.deserialize(jsonObject.get("replies"), object : TypeToken<MutableList<Reply>>() {}.type)
        )
    }
}