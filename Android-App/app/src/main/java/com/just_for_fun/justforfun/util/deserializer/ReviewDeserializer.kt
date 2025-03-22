package com.just_for_fun.justforfun.util.deserializer

import android.app.Application
import android.util.Log
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.just_for_fun.justforfun.R
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

        val avatarResId = try {
            val avatarElement = jsonObject.get("avatarResId")
            if (avatarElement.isJsonPrimitive && avatarElement.asJsonPrimitive.isString) {
                // Handle string resource name
                val resourceName = avatarElement.asString
                val resId = application.resources.getIdentifier(resourceName, "drawable", application.packageName)
                if (resId != 0) resId else R.drawable.placeholder_image
            } else {
                // Handle integer resource ID
                avatarElement.asInt
            }
        } catch (e: Exception) {
            Log.d("ReviewDeserializer", "Error getting avatarResId: ${e.message}")
            R.drawable.placeholder_image // Default fallback
        }

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
