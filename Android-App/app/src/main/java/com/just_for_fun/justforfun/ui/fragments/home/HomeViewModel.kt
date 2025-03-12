package com.just_for_fun.justforfun.ui.fragments.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.data.Movies
import com.just_for_fun.justforfun.data.TVShows

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _movies = MutableLiveData<List<Movies>>()
    val movies: LiveData<List<Movies>> get() = _movies

    private val _tvShows = MutableLiveData<List<TVShows>>()
    val tvShows: LiveData<List<TVShows>> get() = _tvShows

    init {
        loadMovies()
        loadTVShows()
    }

    private fun loadMovies() {
        val moviesList = listOf(
            Movies(
                posterUrl = R.drawable.mughal_e_azam_poster,
                title = "Mughal-e-Azam",
                description = "Akbar and others story.",
                rating = 4.6f,
                type = "Movie",
                director = "K. Asif",
                releaseYear = 1960,
                totalAwards = "3 National Film Awards, 1 Filmfare Award"
            ),
            Movies(
                posterUrl = R.drawable.baazigar_poster,
                title = "Baazigar",
                description = "A young man with a vengeful plan strategically inserts himself into a wealthy businessman's life.",
                rating = 4.6f,
                type = "Movie",
                director = "Abbas-Mustan",
                releaseYear = 1993,
                totalAwards = "4 Filmfare Awards"
            ),
            Movies(
                posterUrl = R.drawable.ddlj_poster,
                title = "Dilwale Dulhania Le Jayenge",
                description = "A young man and woman fall in love on a European trip. When they return to India, the boy's dad challenges him to win the girl on his own.",
                rating = 4.8f,
                type = "Movie",
                director = "Aditya Chopra",
                releaseYear = 1995,
                totalAwards = "10 Filmfare Awards, 1 National Film Award"
            ),
            Movies(
                posterUrl = R.drawable.lagaan_poster,
                title = "Lagaan",
                description = "A village challenges British soldiers to a cricket match to avoid paying high taxes during drought season.",
                rating = 4.5f,
                type = "Movie",
                director = "Ashutosh Gowariker",
                releaseYear = 2001,
                totalAwards = "8 National Film Awards, 8 Filmfare Awards, 1 Oscar Nomination"
            ),
            Movies(
                posterUrl = R.drawable.mm_poster,
                title = "Mr and Mrs 55",
                description = "A cartoonist marries a wealthy woman to save her inheritance, planning to divorce soon after, but love has other plans.",
                rating = 4.3f,
                type = "Movie",
                director = "Guru Dutt",
                releaseYear = 1955,
                totalAwards = "1 Filmfare Award"
            ),
            Movies(
                posterUrl = R.drawable.kranti_poster,
                title = "Kranti",
                description = "A revolutionary fights for freedom from British rule in pre-independence India.",
                rating = 4.2f,
                type = "Movie",
                director = "Manoj Kumar",
                releaseYear = 1981,
                totalAwards = "1 Filmfare Award"
            )
        )
        _movies.value = moviesList
    }

    private fun loadTVShows() {
        val tvShowsList = listOf(
            TVShows(
                posterUrl = R.drawable.got_poster,
                title = "Game of Thrones",
                description = "Noble families vie for control of the Iron Throne as an ancient enemy returns after millennia.",
                rating = 4.7f,
                type = "TV Show",
                totalSeasons = 8,
                totalEpisodes = 73,
                showRunner = "David Benioff, D. B. Weiss",
                yearAired = "2011-2019"
            ),
            TVShows(
                posterUrl = R.drawable.mirzapur_poster,
                title = "Mirzapur",
                description = "A power struggle erupts in the lawless city of Mirzapur, ruled by a ruthless mafia boss.",
                rating = 4.5f,
                type = "TV Show",
                totalSeasons = 3,
                totalEpisodes = 30,
                showRunner = "Karan Anshuman, Puneet Krishna, Gurmmeet Singh",
                yearAired = "2018-2022"
            ),
            TVShows(
                posterUrl = R.drawable.stranger_things_poster,
                title = "Stranger Things",
                description = "A group of kids in a small town uncover a mystery involving secret experiments and supernatural forces.",
                rating = 4.8f,
                type = "TV Show",
                totalSeasons = 5,
                totalEpisodes = 42,
                showRunner = "The Duffer Brothers",
                yearAired = "2016-since"
            ),
            TVShows(
                posterUrl = R.drawable.money_heist_poster,
                title = "Money Heist",
                description = "A criminal mastermind plans an elaborate heist on Spain's Royal Mint with a group of skilled criminals.",
                rating = 4.6f,
                type = "TV Show",
                totalSeasons = 5,
                totalEpisodes = 41,
                showRunner = "Álex Pina",
                yearAired = "2017-2021"
            ),
            TVShows(
                posterUrl = R.drawable.loki_poster,
                title = "Loki",
                description = "The God of Mischief steps out of his brother's shadow and into a time-altering adventure with the TVA.",
                rating = 4.4f,
                type = "TV Show",
                totalSeasons = 2,
                totalEpisodes = 12,
                showRunner = "Michael Waldron",
                yearAired = "2021-2023"
            ),
            TVShows(
                posterUrl = R.drawable.dark_poster,
                title = "Dark",
                description = "A small town's sinister past unfolds as four families become entangled in a time-travel mystery.",
                rating = 4.7f,
                type = "TV Show",
                totalSeasons = 3,
                totalEpisodes = 26,
                showRunner = "Baran bo Odar, Jantje Friese",
                yearAired = "2017-2020"
            )
        )
        _tvShows.value = tvShowsList
    }
}