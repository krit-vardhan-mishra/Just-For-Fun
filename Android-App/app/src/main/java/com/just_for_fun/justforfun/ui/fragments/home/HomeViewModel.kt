package com.just_for_fun.justforfun.ui.fragments.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import java.lang.reflect.Type
import com.just_for_fun.justforfun.data.Movies
import com.just_for_fun.justforfun.data.TVShows
import com.just_for_fun.justforfun.data.TestCases
import okio.IOException
import kotlin.jvm.java

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _movies = MutableLiveData<List<Movies>>()
    val movies: LiveData<List<Movies>> get() = _movies

    private val _tvShows = MutableLiveData<List<TVShows>>()
    val tvShows: LiveData<List<TVShows>> get() = _tvShows

    init {
        loadContentFromJson()
    }

    private fun loadContentFromJson() {
        val jsonString = readJsonFromAssets("sample_cases.json")
        val contentData = parseJsonToContentData(jsonString)
        contentData?.let {
            _movies.value = it.movies
            _tvShows.value = it.tvShows
        }
    }

    private fun readJsonFromAssets(fileName: String): String {
        return try {
            val inputStream = getApplication<Application>().assets.open(fileName)
            inputStream.bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }

    private fun parseJsonToContentData(json: String): TestCases? {
        return try {
            val gson = GsonBuilder()
                .registerTypeAdapter(TestCases::class.java, object : JsonDeserializer<TestCases> {
                    override fun deserialize(
                        json: JsonElement,
                        typeOfT: Type,
                        context: JsonDeserializationContext
                    ): TestCases {
                        val jsonObject = json.asJsonObject

                        val moviesArray = jsonObject.getAsJsonArray("movies")
                        val movies = mutableListOf<Movies>()
                        for (element in moviesArray) {
                            val movieObj = element.asJsonObject
                            val posterString = movieObj.get("posterUrl").asString
                            val resourceName = posterString.replace("R.drawable.", "")
                            val resourceId = getApplication<Application>().resources.getIdentifier(
                                resourceName,
                                "drawable",
                                getApplication<Application>().packageName
                            )
                            movies.add(
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
                            )
                        }

                        val tvShowsArray = jsonObject.getAsJsonArray("tvShows")
                        val tvShows = mutableListOf<TVShows>()
                        for (element in tvShowsArray) {
                            val tvObj = element.asJsonObject
                            val posterString = tvObj.get("posterUrl").asString
                            val resourceName = posterString.replace("R.drawable.", "")
                            val resourceId = getApplication<Application>().resources.getIdentifier(
                                resourceName,
                                "drawable",
                                getApplication<Application>().packageName
                            )
                            tvShows.add(
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
                            )
                        }

                        return TestCases(movies, tvShows)
                    }
                })
                .create()
            gson.fromJson(json, TestCases::class.java)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            null
        }
    }
}