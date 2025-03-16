package com.just_for_fun.justforfun.util.deserializer

import android.app.Application
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.just_for_fun.justforfun.data.Reply
import java.lang.reflect.Type
import java.util.Date

class ReplyDeserializer(private val application: Application) : JsonDeserializer<Reply> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: com.google.gson.JsonDeserializationContext
    ): Reply {
        val jsonObject = json.asJsonObject

        val avatarResIdString = jsonObject.get("avatarResId").asString
        val resourceName = avatarResIdString.replace("R.drawable.", "")
        val avatarResId = application.resources.getIdentifier(
            resourceName, "drawable", application.packageName
        )

        return Reply(
            id = jsonObject.get("id").asString,
            username = jsonObject.get("username").asString,
            avatarResId = avatarResId,
            comment = jsonObject.get("comment").asString,
            date = context.deserialize(jsonObject.get("date"), Date::class.java),
            likedCount = jsonObject.get("likedCount").asInt,
            isLiked = jsonObject.get("isLiked").asBoolean
        )
    }
}