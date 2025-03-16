package com.just_for_fun.justforfun.util.deserializer

import android.content.Context
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.just_for_fun.justforfun.data.CastCrewMember
import java.lang.reflect.Type

class CastCrewDeserializer(private val context: Context) : JsonDeserializer<CastCrewMember> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext?
    ): CastCrewMember {
        val jsonObject = json.asJsonObject

        val id = jsonObject.get("id")?.asString ?: ""

        val imageResourceName = jsonObject.get("castPoster")?.asString ?: ""
        val imageResourceId = this.context.resources?.getIdentifier(
            imageResourceName,
            "drawable",
            this.context.packageName
        ) ?: 0

        val name = jsonObject.get("name")?.asString ?: ""
        val role = jsonObject.get("role")?.asString ?: ""

        return CastCrewMember(
            id = id,
            image = imageResourceId,
            name = name,
            role = role
        )
    }
}