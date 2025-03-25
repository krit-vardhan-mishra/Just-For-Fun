package com.just_for_fun.justforfun.ui.fragments.search

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.just_for_fun.justforfun.data.TVShows
import com.just_for_fun.justforfun.data.TestCases
import com.just_for_fun.justforfun.items.MovieItem
import com.just_for_fun.justforfun.util.deserializer.MoviesSeriesDeserializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    // New LiveData for filtered search results.
    private val _searchResults = MutableLiveData<List<MovieItem>>()
    val searchResults: LiveData<List<MovieItem>> get() = _searchResults

    init {
        loadDataFromJson()
    }

    private fun loadDataFromJson() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val assetManager = getApplication<Application>().assets
                val inputStream: InputStream = assetManager.open("sample_cases.json")
                val jsonString = inputStream.bufferedReader().use { it.readText() }

                val gson = GsonBuilder()
                    .registerTypeAdapter(TestCases::class.java, MoviesSeriesDeserializer(getApplication()))
                    .create()

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

                // Convert TVShows to MovieItem if they share similar properties.
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

                withContext(Dispatchers.Main) {
                    _movies.value = movieItems
                    _tvShows.value = contentData.tvShows

                    _mostSearchedItems.value = allItems.shuffled().take(5)
                    _previousSearches.value = allItems.shuffled().take(5)
                    _basedOnYourSearchItems.value = allItems.shuffled().take(5)
                }
            } catch (e: IOException) {
                Log.e("SearchViewModel", "Error loading data", e)
                withContext(Dispatchers.Main) {
                    _movies.value = emptyList()
                    _tvShows.value = emptyList()
                    _mostSearchedItems.value = emptyList()
                    _previousSearches.value = emptyList()
                    _basedOnYourSearchItems.value = emptyList()
                }
            }
        }
    }

    /**
     * Filters movies and TV shows based on the search query and type filter.
     *
     * @param query The search string entered by the user.
     * @param typeFilter The filter selected ("MOVIE", "TV SHOW", or "ALL").
     */
    fun filterResults(query: String, typeFilter: String) {
        // Combine movies and tvShows (converted to MovieItem)
        val movieList = movies.value.orEmpty()
        val tvShowList = tvShows.value.orEmpty().map { tvShow ->
            MovieItem(
                title = tvShow.title,
                posterUrl = tvShow.posterUrl,
                description = tvShow.description,
                rating = tvShow.rating,
                type = tvShow.type
            )
        }
        val allItems = movieList + tvShowList

        // Filter based on search query and type filter.
        val filtered = allItems.filter { item ->
            val matchesType = when (typeFilter.uppercase()) {
                "MOVIE" -> item.type.equals("Movie", ignoreCase = true)
                "TV SHOW" -> item.type.equals("TV Show", ignoreCase = true)
                else -> true // "ALL" or any unknown selection.
            }
            val matchesQuery = item.title.contains(query, ignoreCase = true)
            matchesType && matchesQuery
        }
        _searchResults.value = filtered
    }
}
