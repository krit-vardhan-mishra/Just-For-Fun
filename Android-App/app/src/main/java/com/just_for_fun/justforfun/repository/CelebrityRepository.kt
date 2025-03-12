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
            posterUrl = R.drawable.mm_poster,
            description = "Classic romantic comedy",
            rating = 4.5f,
            type = "Movie"
        ),
        MovieItem(
            title = "Mughal-e-Azam",
            posterUrl = R.drawable.mughal_e_azam_poster,
            description = "Epic historical drama",
            rating = 4.8f,
            type = "Movie"
        )
    )

    private val sampleTVShows = listOf(
        MovieItem(
            title = "Historical Dramas",
            posterUrl = R.drawable.kranti_poster,
            description = "TV documentary series",
            rating = 4.2f,
            type = "TV Show"
        )
    )

    private val sampleAwards = listOf(
        Awards("1", "Best Actor", 2020, "Golden Globe Award for Best Motion Picture – Drama", "For this movie", "Awarded for best performance by an actor"),
        Awards("2", "Best Actress", 2020, "Academy Award for Best Animated Feature Film","For this movie", "Awarded for best performance by an actress"),
        Awards("3", "Lifetime Achievement", 2021, "Prime time Emmy Award for Outstanding Lead Actor in a Drama Series", "For this tv show", "Awarded for outstanding career achievements"),
        Awards("4", "Best Director", 2022, "Grammy Award for Album of the Year","For this movie", "Awarded for best direction in a movie"),
        Awards("5", "Best Supporting Actor", 2020, "Tony Award for Best Play", "For this movie", "Awarded for best supporting actor"),
        Awards("6", "Best Newcomer", 2021, "BAFTA Award for Best British Film", "For best actor in this movie", "Awarded for best debut performance"),
        Awards("7", "Best Comedian", 2022, "Cannes Film Festival Palme d'Or", "For best actor in this movie", "Awarded for best comedic performance"),
        Awards("8", "Best TV Show", 2024, "Critics' Choice Television Award for Best Comedy Series", "For best tv show", "Awarded for best comedic performance")
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