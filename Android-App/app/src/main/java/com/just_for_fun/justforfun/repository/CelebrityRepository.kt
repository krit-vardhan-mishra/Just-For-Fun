package com.just_for_fun.justforfun.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.data.Awards
import com.just_for_fun.justforfun.data.Celebrity
import com.just_for_fun.justforfun.items.MovieItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CelebrityRepository() {

    private val sampleCelebrity = Celebrity(
        id = "1",
        name = "Madhubala",
        age = 32,
        bio = "Legendary Bollywood actress",
        imageUrl = "drawable/cast_one",
        filmographyCount = 72,
        awardsCount = 15
    )

    private val sampleMovies = listOf(
        MovieItem(
            title = "Mr. & Mrs. 55",
            posterUrl = "R.drawable.mm_poster",
            description = "Classic romantic comedy",
            rating = 4.5,
            type = "Movie"
        ),
        MovieItem(
            title = "Mughal-e-Azam",
            posterUrl = "R.drawable.mughal_e_azam_poster",
            description = "Epic historical drama",
            rating = 4.8,
            type = "Movie"
        )
    )

    private val sampleTVShows = listOf(
        MovieItem(
            title = "Historical Dramas",
            posterUrl = "R.drawable.kranti_poster",
            description = "TV documentary series",
            rating = 4.2,
            type = "TV Show"
        )
    )

    private val sampleAwards = listOf(
        Awards(
            id = "1",
            name = "Best Actress",
            year = 1960,
            category = "Filmfare Awards"
        ),
        Awards(
            id = "2",
            name = "Lifetime Achievement",
            year = 1990,
            category = "National Awards"
        )
    )

    private val _celebrity = MutableLiveData<Celebrity>()
    private val _movies = MutableLiveData<List<MovieItem>>()
    private val _tvShows = MutableLiveData<List<MovieItem>>()
    private val _awards = MutableLiveData<List<Awards>>()

    val celebrity: LiveData<Celebrity> get() = _celebrity
    val movies: LiveData<List<MovieItem>> get() = _movies
    val tvShows: LiveData<List<MovieItem>> get() = _tvShows
    val awards: LiveData<List<Awards>> get() = _awards

    init {
        CoroutineScope(Dispatchers.IO).launch {
            loadData()
        }
    }

    private suspend fun loadData() {
        delay(1000)
        _celebrity.postValue(sampleCelebrity)
        _movies.postValue(sampleMovies)
        _tvShows.postValue(sampleTVShows)
        _awards.postValue(sampleAwards)
    }
}