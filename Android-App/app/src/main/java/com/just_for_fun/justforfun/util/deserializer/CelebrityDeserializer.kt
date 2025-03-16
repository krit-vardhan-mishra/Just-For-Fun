package com.just_for_fun.justforfun.util.deserializer

import android.content.Context
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.just_for_fun.justforfun.data.Celebrity
import java.lang.reflect.Type

class CelebrityDeserializer(private val context: Context) : JsonDeserializer<Celebrity> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        jsonDeserializationContext: JsonDeserializationContext?
    ): Celebrity {
        val jsonObject = json.asJsonObject

        val id = jsonObject.get("id")?.asString ?: ""
        val name = jsonObject.get("name")?.asString ?: ""
        val age = jsonObject.get("age")?.asInt ?: 0
        val bio = jsonObject.get("bio")?.asString ?: ""

        val imageResourceName = jsonObject.get("imageUrl")?.asString ?: ""
        val imageResourceId = context.resources.getIdentifier(
            imageResourceName,
            "drawable",
            context.packageName
        )

        val filmographyCount = jsonObject.get("filmographyCount")?.asInt ?: 0
        val awardsCount = jsonObject.get("awardsCount")?.asInt ?: 0

        return Celebrity(
            id = id,
            name = name,
            age = age,
            bio = bio,
            imageUrl = imageResourceId,
            filmographyCount = filmographyCount,
            awardsCount = awardsCount
        )
    }
}
