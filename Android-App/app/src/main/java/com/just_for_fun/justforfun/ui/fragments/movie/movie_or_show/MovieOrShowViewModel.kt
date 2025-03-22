package com.just_for_fun.justforfun.ui.fragments.movie.movie_or_show

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.just_for_fun.justforfun.data.CastCrewMember
import com.just_for_fun.justforfun.data.TestCases
import com.just_for_fun.justforfun.items.MovieItem
import com.just_for_fun.justforfun.util.deserializer.CastCrewDeserializer
import com.just_for_fun.justforfun.util.deserializer.MoviesSeriesDeserializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieOrShowViewModel(application: Application) : AndroidViewModel(application) {
    private val _movieDetails = MutableLiveData<MovieDetails>()
    val movieDetails: LiveData<MovieDetails> get() = _movieDetails

    private val _castAndCrew = MutableLiveData<List<CastCrewMember>>()
    val castAndCrew: LiveData<List<CastCrewMember>> get() = _castAndCrew

    private val _similarMovies = MutableLiveData<List<MovieItem>>()
    val similarMovies: LiveData<List<MovieItem>> get() = _similarMovies

    private val _testCases = MutableLiveData<TestCases?>()
    val testCases: LiveData<TestCases?> get() = _testCases

    private val _selectedCastMember = MutableLiveData<CastCrewMember?>()
    val selectedCastMember: LiveData<CastCrewMember?> get() = _selectedCastMember

    init {
        loadTestCases()
    }

    fun setMovieDetails(title: String, posterUrl: Int, description: String, rating: Float, type: String) {
        _movieDetails.value = MovieDetails(title, posterUrl, description, rating, type)
    }

    fun selectCastMember(castMember: CastCrewMember) {
        _selectedCastMember.value = castMember
    }

    fun onCastMemberNavigated() {
        _selectedCastMember.value = null
    }

    fun loadCastAndCrew() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val jsonString = getApplication<Application>().assets.open("cast_crew.json")
                    .bufferedReader().use { it.readText() }

                val gson = GsonBuilder()
                    .registerTypeAdapter(CastCrewMember::class.java, CastCrewDeserializer(getApplication()))
                    .create()

                val listType = object : TypeToken<List<CastCrewMember>>() {}.type
                val castList = gson.fromJson(jsonString, listType) as List<CastCrewMember>

                withContext(Dispatchers.Main) {
                    _castAndCrew.value = castList
                }
            } catch (e: Exception) {
                Log.e("MovieOrShowViewModel", "Error loading cast and crew", e)
            }
        }
    }

    private fun loadTestCases() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val jsonString = getApplication<Application>().assets.open("sample_cases.json")
                    .bufferedReader().use { it.readText() }

                val gson = GsonBuilder()
                    .registerTypeAdapter(TestCases::class.java, MoviesSeriesDeserializer(getApplication()))
                    .create()

                val testCasesData = gson.fromJson(jsonString, TestCases::class.java)
                withContext(Dispatchers.Main) {
                    _testCases.value = testCasesData
                }
            } catch (e: Exception) {
                Log.e("MovieOrShowViewModel", "Error loading test cases", e)
            }
        }
    }

    fun setupMoreLikeThis(movieType: String) {
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
            _similarMovies.postValue(similarMovies)
        }
    }

    data class MovieDetails(
        val title: String,
        val posterUrl: Int,
        val description: String,
        val rating: Float,
        val type: String
    )
}