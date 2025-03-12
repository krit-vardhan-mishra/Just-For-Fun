package com.just_for_fun.justforfun.ui.fragments.search

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.reflect.TypeToken
import com.just_for_fun.justforfun.data.TVShows
import com.just_for_fun.justforfun.data.TestCases
import com.just_for_fun.justforfun.items.MovieItem
import java.io.IOException
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.just_for_fun.justforfun.data.Movies
import java.lang.reflect.Type
class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val _movies = MutableLiveData<List<MovieItem>>()
    val movies: LiveData<List<MovieItem>> get() = _movies

    private val _tvShows = MutableLiveData<List<TVShows>>()
    val tvShows: LiveData<List<TVShows>> get() = _tvShows

    private val _mostSearchedItems = MutableLiveData<List<MovieItem>>()
    val mostSearchedItems: LiveData<List<MovieItem>> get() = _mostSearchedItems

    private val _previousSearches = MutableLiveData<List<MovieItem>>()
    val previousSearches: LiveData<List<MovieItem>> get() = _previousSearches

    private val _basedOnYourSearchItems = MutableLiveData<List<MovieItem>>()
    val basedOnYourSearchItems: LiveData<List<MovieItem>> get() = _basedOnYourSearchItems

    init {
        loadDataFromJson()
    }

    private fun loadDataFromJson() {
        try {
            val assetManager = getApplication<Application>().assets
            val inputStream = assetManager.open("sample_cases.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }

            val gson = GsonBuilder()
                .registerTypeAdapter(TestCases::class.java, object : JsonDeserializer<TestCases> {
                    override fun deserialize(
                        json: JsonElement,
                        typeOfT: Type,
                        context: JsonDeserializationContext
                    ): TestCases {
                        val jsonObject = json.asJsonObject
                        val moviesArray = jsonObject.getAsJsonArray("movies")
                        val tvShowsArray = jsonObject.getAsJsonArray("tvShows")

                        val movies = mutableListOf<Movies>()
                        val tvShows = mutableListOf<TVShows>()

                        for (element in moviesArray) {
                            val movieObj = element.asJsonObject
                            val resourceName = movieObj.get("posterUrl").asString.replace("R.drawable.", "")
                            val resourceId = getApplication<Application>().resources.getIdentifier(
                                resourceName, "drawable", getApplication<Application>().packageName
                            )

                            val movie = Movies(
                                posterUrl = resourceId,
                                title = movieObj.get("title").asString,
                                description = movieObj.get("description").asString,
                                rating = movieObj.get("rating").asFloat,
                                type = movieObj.get("type").asString,
                                director = movieObj.get("director").asString,
                                releaseYear = movieObj.get("releaseYear").asInt,
                                totalAwards = movieObj.get("totalAwards").asString
                            )
                            movies.add(movie)
                        }

                        for (element in tvShowsArray) {
                            val tvObj = element.asJsonObject
                            val resourceName = tvObj.get("posterUrl").asString.replace("R.drawable.", "")
                            val resourceId = getApplication<Application>().resources.getIdentifier(
                                resourceName, "drawable", getApplication<Application>().packageName
                            )

                            val tvShow = TVShows(
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
                            tvShows.add(tvShow)
                        }

                        return TestCases(movies, tvShows)
                    }
                })
                .create()

            val contentType = object : TypeToken<TestCases>() {}.type
            val contentData: TestCases = gson.fromJson(jsonString, contentType)

            val movieItems = contentData.movies.map { movies ->
                MovieItem(
                    title = movies.title,
                    posterUrl = movies.posterUrl,
                    description = movies.description,
                    rating = movies.rating,
                    type = movies.type
                )
            }

            val tvShowItems = contentData.tvShows.map { tvShows ->
                MovieItem(
                    title = tvShows.title,
                    posterUrl = tvShows.posterUrl,
                    description = tvShows.description,
                    rating = tvShows.rating,
                    type = tvShows.type
                )
            }

            val allItems = movieItems + tvShowItems

            _movies.value = movieItems
            _tvShows.value = contentData.tvShows

            _mostSearchedItems.value = allItems.shuffled().take(5)
            _previousSearches.value = allItems.shuffled().take(5)
            _basedOnYourSearchItems.value = allItems.shuffled().take(5)

        } catch (e: IOException) {
            Log.e("SearchViewModel", "Error loading data", e)

            _movies.value = emptyList()
            _tvShows.value = emptyList()
            _mostSearchedItems.value = emptyList()
            _previousSearches.value = emptyList()
            _basedOnYourSearchItems.value = emptyList()
        }
    }
}
