package com.just_for_fun.justforfun.ui.fragments.search

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.just_for_fun.justforfun.data.TVShows
import com.just_for_fun.justforfun.data.TestCases
import com.just_for_fun.justforfun.items.MovieItem
import java.io.IOException

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

            val gson = Gson()
            val contentType = object : TypeToken<TestCases>() {}.type
            val contentData: TestCases = gson.fromJson(jsonString, contentType)

            val movieItems = contentData.movies.map { movie ->
                MovieItem(
                    title = movie.title,
                    posterUrl = movie.posterUrl,
                    description = movie.description,
                    rating = movie.rating,
                    type = movie.type
                )
            }

            val tvShowItems = contentData.tvShows.map { tvShow ->
                MovieItem(
                    title = tvShow.title,
                    posterUrl = tvShow.posterUrl,
                    description = tvShow.description,
                    rating = tvShow.rating,
                    type = tvShow.type
                )
            }

            val allItems = movieItems + tvShowItems

            _movies.value = movieItems
            _tvShows.value = contentData.tvShows

            _mostSearchedItems.value = allItems.shuffled().take(5)
            _previousSearches.value = allItems.shuffled().take(5)
            _basedOnYourSearchItems.value = allItems.shuffled().take(5)

        } catch (e: IOException) {
            Log.d("LoadDataFromJSON", e.printStackTrace().toString())

            _movies.value = emptyList()
            _tvShows.value = emptyList()
            _mostSearchedItems.value = emptyList()
            _previousSearches.value = emptyList()
            _basedOnYourSearchItems.value = emptyList()
        }
    }
}
