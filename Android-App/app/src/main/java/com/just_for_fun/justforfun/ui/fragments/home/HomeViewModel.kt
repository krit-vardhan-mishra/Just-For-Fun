package com.just_for_fun.justforfun.ui.fragments.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.just_for_fun.justforfun.data.Movies
import com.just_for_fun.justforfun.data.TVShows
import com.just_for_fun.justforfun.data.TestCases
import java.io.IOException

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _movies = MutableLiveData<List<Movies>>()
    val movies: LiveData<List<Movies>> get() = _movies

    private val _tvShows = MutableLiveData<List<TVShows>>()
    val tvShows: LiveData<List<TVShows>> get() = _tvShows

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

            _movies.value = contentData.movies
            _tvShows.value = contentData.tvShows
        } catch (e: IOException) {
            e.printStackTrace()
            _movies.value = emptyList()
            _tvShows.value = emptyList()
        }
    }
}