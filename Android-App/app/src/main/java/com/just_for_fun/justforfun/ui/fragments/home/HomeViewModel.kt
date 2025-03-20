package com.just_for_fun.justforfun.ui.fragments.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import java.lang.reflect.Type
import com.just_for_fun.justforfun.data.Movies
import com.just_for_fun.justforfun.data.TVShows
import com.just_for_fun.justforfun.data.TestCases
import com.just_for_fun.justforfun.util.deserializer.MoviesSeriesDeserializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.IOException
import kotlin.jvm.java

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _movies = MutableLiveData<List<Movies>>()
    val movies: LiveData<List<Movies>> get() = _movies

    private val _tvShows = MutableLiveData<List<TVShows>>()
    val tvShows: LiveData<List<TVShows>> get() = _tvShows

    init {
        loadContentFromJson()
    }

    private fun loadContentFromJson() {
        viewModelScope.launch(Dispatchers.IO) {
            val jsonString = readJsonFromAssets("sample_cases.json")
            val contentData = parseJsonToContentData(jsonString)
            withContext(Dispatchers.Main) {
                contentData?.let {
                    _movies.value = it.movies
                    _tvShows.value = it.tvShows
                }
            }
        }
    }

    private fun readJsonFromAssets(fileName: String): String {
        return try {
            getApplication<Application>().assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }

    private fun parseJsonToContentData(json: String): TestCases? {
        return try {
            val gson = GsonBuilder()
                .registerTypeAdapter(TestCases::class.java, MoviesSeriesDeserializer(getApplication()))
                .create()
            gson.fromJson(json, TestCases::class.java)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            null
        }
    }
}