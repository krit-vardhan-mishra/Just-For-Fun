package com.just_for_fun.justforfun.util.deserializer

import android.app.Application
import android.util.Log
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.data.Reply
import com.just_for_fun.justforfun.data.Review
import java.lang.reflect.Type
import java.util.Date
import kotlin.random.Random

class ReviewDeserializer(private val application: Application) : JsonDeserializer<Review> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Review {
        val jsonObject = json.asJsonObject

        // Handle string ID
        val id = try {
            val idElement = jsonObject.get("id")
            if (idElement.isJsonPrimitive && idElement.asJsonPrimitive.isString) {
                idElement.asString.toIntOrNull() ?: Random.nextInt(1000, 9999)
            } else {
                idElement.asInt
            }
        } catch (e: Exception) {
            Log.d("ReviewDeserializer", "Error getting id: ${e.message}")
            Random.nextInt(1000, 9999) // Default fallback
        }

        // Handle string avatarResId
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
            Log.d("ReviewDeserializer", "Error getting avatarResId: ${e.message}")
            application.resources.getIdentifier("placeholder_image", "drawable", application.packageName)
        }

        // Use safe parsing for the replies
        val replies = try {
            val repliesArray = jsonObject.getAsJsonArray("replies")
            val replyList = mutableListOf<Reply>()

            for (replyElement in repliesArray) {
                val replyObj = replyElement.asJsonObject

                val replyAvatarResId = try {
                    val resourceName = replyObj.get("avatarResId").asString
                    application.resources.getIdentifier(resourceName, "drawable", application.packageName)
                } catch (e: Exception) {
                    R.drawable.placeholder_image
                }

                val reply = Reply(
                    id = replyObj.get("id").asString,
                    username = replyObj.get("username").asString,
                    avatarResId = replyAvatarResId,
                    comment = replyObj.get("comment").asString,
                    date = context.deserialize(replyObj.get("date"), Date::class.java),
                    likeCount = replyObj.get("likeCount").asInt,
                    isLiked = replyObj.get("isLiked").asBoolean
                )
                replyList.add(reply)
            }

            replyList
        } catch (e: Exception) {
            Log.e("ReviewDeserializer", "Error parsing replies: ${e.message}")
            mutableListOf()
        }

        return Review(
            id = id.toString(),
            username = jsonObject.get("username").asString,
            avatarResId = avatarResId,
            comment = jsonObject.get("comment").asString,
            rating = jsonObject.get("rating").asFloat,
            date = context.deserialize(jsonObject.get("date"), Date::class.java),
            likeCount = jsonObject.get("likeCount").asInt,
            isLiked = jsonObject.get("isLiked").asBoolean,
            replies = replies
        )
    }
}