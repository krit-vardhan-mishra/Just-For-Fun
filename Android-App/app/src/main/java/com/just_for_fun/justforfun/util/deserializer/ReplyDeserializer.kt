package com.just_for_fun.justforfun.util.deserializer

import android.app.Application
import android.util.Log
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.just_for_fun.justforfun.data.Reply
import java.lang.reflect.Type
import java.util.Date

class ReplyDeserializer(private val application: Application) : JsonDeserializer<Reply> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Reply {
        val jsonObject = json.asJsonObject

        val avatarResId = try {
            val avatarElement = jsonObject.get("avatarResId")
            if (avatarElement.isJsonPrimitive && avatarElement.asJsonPrimitive.isString) {
                // Handle string resource name
                val resourceName = avatarElement.asString
                application.resources.getIdentifier(resourceName, "drawable", application.packageName)
            } else {
                // Handle integer resource ID
                avatarElement.asInt
            }
        } catch (e: Exception) {
            Log.d("ReplyDeserializer", "Error getting avatarResId: ${e.message}")
            application.resources.getIdentifier("placeholder_image", "drawable", application.packageName)
        }

        // Keep the ID as string as that's how it is in your JSON
        return Reply(
            id = jsonObject.get("id").asString,
            username = jsonObject.get("username").asString,
            avatarResId = avatarResId,
            comment = jsonObject.get("comment").asString,
            date = context.deserialize(jsonObject.get("date"), Date::class.java),
            likeCount = jsonObject.get("likeCount").asInt,
            isLiked = jsonObject.get("isLiked").asBoolean
        )
    }
}