package com.just_for_fun.justforfun.ui.fragments.movie.movie_or_show

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.just_for_fun.justforfun.data.TestCases
import com.just_for_fun.justforfun.items.MovieItem
import com.just_for_fun.justforfun.util.deserializer.MoviesSeriesDeserializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieOrShowViewModel(application: Application) : AndroidViewModel(application) {

    private val _similarMovies = MutableLiveData<List<MovieItem>>()
    val similarMovies: LiveData<List<MovieItem>> get() = _similarMovies

    private val _testCases = MutableLiveData<TestCases?>()
    val testCases: LiveData<TestCases?> get() = _testCases

    private fun loadTestCases() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val assetManager = getApplication<Application>().assets
                Log.d("MovieViewModel", "Loading test cases from assets")

                val inputStream = assetManager.open("sample_cases.json")
                val jsonString = inputStream.bufferedReader().use { it.readText() }

                val gson = GsonBuilder()
                    .registerTypeAdapter(TestCases::class.java, MoviesSeriesDeserializer(getApplication()))
                    .create()
                val testCasesData = gson.fromJson(jsonString, TestCases::class.java)
                withContext(Dispatchers.Main) {
                    _testCases.value = testCasesData
                }
                Log.d("MovieViewModel", "Test cases parsed successfully")
            } catch (e: Exception) {
                Log.e("MovieViewModel", "Error parsing sample_cases JSON", e)
                _testCases.postValue(null)
            }
        }
    }

    fun setupMoreLikeThis(movieType: String) {
        Log.d("MovieViewModel", "Setting up more like this for type: $movieType")
        Log.d("MovieViewModel", "Test cases available: ${_testCases.value != null}")

        _testCases.value?.let { testCases ->
            val similarMovies = if (movieType == "Movie") {
                testCases.movies.map { movie ->
                    MovieItem(
                        posterUrl = movie.posterUrl,
                        title = movie.title,
                        description = movie.description,
                        rating = movie.rating,
                        type = movie.type
                    )
                }
            } else {
                testCases.tvShows.map { tvShow ->
                    MovieItem(
                        posterUrl = tvShow.posterUrl,
                        title = tvShow.title,
                        description = tvShow.description,
                        rating = tvShow.rating,
                        type = tvShow.type
                    )
                }
            }
            Log.d("MovieViewModel", "Similar movies count: ${similarMovies.size}")
            _similarMovies.postValue(similarMovies)
        } ?: run {
            Log.e("MovieViewModel", "Test cases are null, cannot load similar movies")
        }
    }

}