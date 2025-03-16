package com.just_for_fun.justforfun.ui.fragments.search

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.just_for_fun.justforfun.data.TVShows
import com.just_for_fun.justforfun.data.TestCases
import com.just_for_fun.justforfun.items.MovieItem
import com.just_for_fun.justforfun.util.deserializer.MoviesSeriesDeserializer
import java.io.IOException
import java.io.InputStream

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
            val inputStream: InputStream = assetManager.open("sample_cases.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }

            val gson = GsonBuilder()
                .registerTypeAdapter(TestCases::class.java, MoviesSeriesDeserializer(getApplication()))
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
