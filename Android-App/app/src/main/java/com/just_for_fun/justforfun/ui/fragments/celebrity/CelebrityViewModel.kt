package com.just_for_fun.justforfun.ui.fragments.celebrity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.just_for_fun.justforfun.data.Celebrity
import com.just_for_fun.justforfun.data.Awards
import com.just_for_fun.justforfun.items.MovieItem
import com.just_for_fun.justforfun.repository.CelebrityRepository

class CelebrityViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CelebrityRepository(application)

    val celebrity: LiveData<Celebrity> = repository.celebrity
    val movies: LiveData<List<MovieItem>> = repository.movies
    val tvShows: LiveData<List<MovieItem>> = repository.tvShows
    val awards: LiveData<List<Awards>> = repository.awards

    fun setCelebrityId(id: String) {
        repository.setCelebrityId(id)
    }
}