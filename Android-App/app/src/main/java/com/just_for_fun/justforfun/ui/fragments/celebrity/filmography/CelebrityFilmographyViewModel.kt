package com.just_for_fun.justforfun.ui.fragments.celebrity.filmography

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.items.MovieItem

class CelebrityFilmographyViewModel : ViewModel() {
    private val _filmography = MutableLiveData<List<MovieItem>>()
    val filmography: MutableLiveData<List<MovieItem>> get() = _filmography

    fun loadFilmography(type: String) {
        val sampleData = when (type) {
            "movies" -> listOf(
                MovieItem(
                    R.drawable.mughal_e_azam_poster,
                    "Mugle-a-azam",
                    "Akbar and others story.",
                    4.6f,
                    "Movie"
                ),
                MovieItem(
                    R.drawable.baazigar_poster,
                    "Baazigar",
                    "A young man with a vengeful plan strategically inserts himself into a wealthy businessman's life.",
                    4.6f,
                    "Movie"
                ),
                MovieItem(
                    R.drawable.ddlj_poster,
                    "Dilwale Dulhania Le Jayenge",
                    "A young man and woman fall in love on a European trip. When they return to India, the boy's dad challenges him to win the girl on his own.",
                    4.8f,
                    "Movie"
                ),
                MovieItem(
                    R.drawable.lagaan_poster,
                    "Lagaan",
                    "A village challenges British soldiers to a cricket match to avoid paying high taxes during drought season.",
                    4.5f,
                    "Movie"
                ),
                MovieItem(
                    R.drawable.mm_poster,
                    "Mr and Mrs 55",
                    "A cartoonist marries a wealthy woman to save her inheritance, planning to divorce soon after, but love has other plans.",
                    4.3f,
                    "Movie"
                ),
                MovieItem(
                    R.drawable.kranti_poster,
                    "Kranti",
                    "A revolutionary fights for freedom from British rule in pre-independence India.",
                    4.2f,
                    "Movie"
                )
            )
            else -> listOf(
                MovieItem(R.drawable.kranti_poster, "TV Show 1", "Desc 3", 4.2f, "TV Show"),
                MovieItem(R.drawable.ddlj_poster, "TV Show 2", "Desc 4", 4.1f, "TV Show")
            )
        }
        _filmography.value = sampleData
    }
}