package com.just_for_fun.justforfun.util.deserializer

import android.content.Context
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.just_for_fun.justforfun.data.Movies
import com.just_for_fun.justforfun.data.TVShows
import com.just_for_fun.justforfun.data.TestCases
import java.lang.reflect.Type

class MoviesSeriesDeserializer(private val context: Context) : JsonDeserializer<TestCases> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): TestCases {
        val jsonObject = json.asJsonObject
        val moviesArray = jsonObject.getAsJsonArray("movies")
        val tvShowsArray = jsonObject.getAsJsonArray("tvShows")

        val movies = moviesArray.map { element ->
            val movieObj = element.asJsonObject
            val resourceName = movieObj.get("posterUrl").asString.replace("R.drawable.", "")
            val resourceId = this.context.resources.getIdentifier(resourceName, "drawable", this.context.packageName)

            Movies(
                posterUrl = resourceId,
                title = movieObj.get("title").asString,
                description = movieObj.get("description").asString,
                rating = movieObj.get("rating").asFloat,
                type = movieObj.get("type").asString,
                director = movieObj.get("director").asString,
                releaseYear = movieObj.get("releaseYear").asInt,
                totalAwards = movieObj.get("totalAwards").asString
            )
        }

        val tvShows = tvShowsArray.map { element ->
            val tvObj = element.asJsonObject
            val resourceName = tvObj.get("posterUrl").asString.replace("R.drawable.", "")
            val resourceId = this.context.resources.getIdentifier(resourceName, "drawable", this.context.packageName)

            TVShows(
                posterUrl = resourceId,
                title = tvObj.get("title").asString,
                description = tvObj.get("description").asString,
                rating = tvObj.get("rating").asFloat,
                type = tvObj.get("type").asString,
                totalSeasons = tvObj.get("totalSeasons").asInt,
                totalEpisodes = tvObj.get("totalEpisodes").asInt,
                showRunner = tvObj.get("showRunner").asString,
                yearAired = tvObj.get("yearAired").asString
            )
        }

        return TestCases(movies, tvShows)
    }
}
