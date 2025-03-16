package com.just_for_fun.justforfun.ui.fragments.movie

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.just_for_fun.justforfun.data.CastCrewMember
import com.just_for_fun.justforfun.data.Review
import com.just_for_fun.justforfun.data.Reply
import com.just_for_fun.justforfun.data.TestCases
import com.just_for_fun.justforfun.items.MovieItem
import com.just_for_fun.justforfun.util.deserializer.MoviesSeriesDeserializer
import com.just_for_fun.justforfun.util.deserializer.ReplyDeserializer
import com.just_for_fun.justforfun.util.deserializer.ReviewDeserializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieViewModel(application: Application) : AndroidViewModel(application) {
    private val _isTvShow = MutableLiveData<Boolean>(false)
    val isTvShow: LiveData<Boolean> get() = _isTvShow

    private val _title = MutableLiveData<String>("Default Title")
    val title: LiveData<String> get() = _title

    private val _description = MutableLiveData<String>("Default description about the content.")
    val description: LiveData<String> get() = _description

    private val _rating = MutableLiveData<Float>(0f)
    val rating: LiveData<Float> get() = _rating

    private val _totalRating = MutableLiveData<Float>(0f)
    val totalRating: LiveData<Float> get() = _totalRating

    private val _seasons = MutableLiveData<Int>(0)
    val seasons: LiveData<Int> get() = _seasons

    private val _episodes = MutableLiveData<Int>(0)
    val episodes: LiveData<Int> get() = _episodes

    private val _selectedCastMember = MutableLiveData<CastCrewMember?>()
    val selectedCastMember: LiveData<CastCrewMember?> get() = _selectedCastMember

    private val _testCases = MutableLiveData<TestCases?>()
    val testCases: LiveData<TestCases?> get() = _testCases

    private val _reviews = MutableLiveData<List<Review>?>()
    val reviews: LiveData<List<Review>?> get() = _reviews

    private val _similarMovies = MutableLiveData<List<MovieItem>>()
    val similarMovies: LiveData<List<MovieItem>> get() = _similarMovies

    init {
        loadTestCases()
        loadReviewsData()
    }

    fun selectCastMember(castMember: CastCrewMember) {
        _selectedCastMember.value = castMember
    }

    fun onCastMemberNavigated() {
        _selectedCastMember.value = null
    }

    private fun loadTestCases() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val assetManager = getApplication<Application>().assets
                Log.d("MovieViewModel", "Loading test cases from assets")

                val inputStream = assetManager.open("sample_cases.json")
                val jsonString = inputStream.bufferedReader().use { it.readText() }
                Log.d("MovieViewModel", "JSON read successfully: ${jsonString.take(100)}...")

                val gson = GsonBuilder()
                    .registerTypeAdapter(TestCases::class.java, MoviesSeriesDeserializer(getApplication()))
                    .create()
                val testCasesData = gson.fromJson(jsonString, TestCases::class.java)
                Log.d("MovieViewModel", "Test cases parsed successfully")

                _testCases.postValue(testCasesData)
            } catch (e: Exception) {
                Log.e("MovieViewModel", "Error parsing sample_cases JSON", e)
                _testCases.postValue(null)
            }
        }
    }

    private fun loadReviewsData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val assetManager = getApplication<Application>().assets
                val inputStream = assetManager.open("reviews.json")
                val jsonString = inputStream.bufferedReader().use { it.readText() }
                val gson = GsonBuilder()
                    .registerTypeAdapter(Review::class.java, ReviewDeserializer(getApplication()))
                    .registerTypeAdapter(Reply::class.java, ReplyDeserializer(getApplication()))
                    .create()
                val type = object : TypeToken<List<Review>>() {}.type
                val reviewsList: List<Review> = gson.fromJson(jsonString, type)
                _reviews.postValue(reviewsList)
            } catch (e: Exception) {
                Log.e("MovieViewModel", "Error parsing reviews JSON", e)
                _reviews.postValue(null)
            }
        }
    }

    fun setupMoreLikeThis(movieType: String) {
        Log.d("MovieViewModel", "Setting up more like this for type: $movieType")
        Log.d("MovieViewModel", "Test cases available: ${_testCases.value != null}")

        _testCases.value?.let { testCases ->
            val similarMovies = if (movieType == "Movie") {
                testCases.movies?.map { movie ->
                    MovieItem(
                        posterUrl = movie.posterUrl,
                        title = movie.title,
                        description = movie.description,
                        rating = movie.rating,
                        type = movie.type
                    )
                } ?: emptyList()
            } else {
                testCases.tvShows?.map { tvShow ->
                    MovieItem(
                        posterUrl = tvShow.posterUrl,
                        title = tvShow.title,
                        description = tvShow.description,
                        rating = tvShow.rating,
                        type = tvShow.type
                    )
                } ?: emptyList()
            }
            Log.d("MovieViewModel", "Similar movies count: ${similarMovies.size}")
            _similarMovies.postValue(similarMovies)
        } ?: run {
            Log.e("MovieViewModel", "Test cases are null, cannot load similar movies")
        }
    }
}