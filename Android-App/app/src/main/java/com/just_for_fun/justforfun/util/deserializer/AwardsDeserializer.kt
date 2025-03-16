package com.just_for_fun.justforfun.util.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.just_for_fun.justforfun.data.Awards
import java.lang.reflect.Type

class AwardsDeserializer : JsonDeserializer<Awards> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Awards {
        val jsonObject = json.asJsonObject

        val id = if (jsonObject.get("id").isJsonPrimitive) {
            jsonObject.get("id").asString
        } else {
            ""
        }

        return Awards(
            id = id,
            name = jsonObject.get("name").asString,
            year = jsonObject.get("year").asInt,
            awardName = jsonObject.get("awardName").asString,
            category = jsonObject.get("category").asString,
            description = jsonObject.get("description").asString
        )
    }
}
