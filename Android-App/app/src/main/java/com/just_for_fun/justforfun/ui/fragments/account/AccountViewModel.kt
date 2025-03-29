package com.just_for_fun.justforfun.ui.fragments.account

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.just_for_fun.justforfun.data.Movies
import com.just_for_fun.justforfun.data.TVShows
import com.just_for_fun.justforfun.data.TestCases
import com.just_for_fun.justforfun.data.UserLoginData
import com.just_for_fun.justforfun.util.deserializer.MoviesSeriesDeserializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.IOException
import java.io.File

class AccountViewModel(application: Application) : AndroidViewModel(application) {
    private val _currentUserLoginData = MutableLiveData<UserLoginData?>()
    val currentUserLoginData: LiveData<UserLoginData?> = _currentUserLoginData

    private val _combinedContent = MutableLiveData<List<Int>>()
    val combinedContent: LiveData<List<Int>> = _combinedContent

    private val _movies = MutableLiveData<List<Movies>>()
    val movies: LiveData<List<Movies>> get() = _movies

    private val _tvShows = MutableLiveData<List<TVShows>>()
    val tvShows: LiveData<List<TVShows>> get() = _tvShows

    private val FILE_NAME = "user.json"
    private val gson = Gson()


    init {
        loadContentFromJson()
        loadCurrentUser()
    }

    private fun loadContentFromJson() {
        viewModelScope.launch(Dispatchers.IO) {
            val jsonString = readJsonFromAssets("sample_cases.json")
            val contentData = parseJsonToContentData(jsonString)
            withContext(Dispatchers.Main) {
                contentData?.let {
                    _movies.value = it.movies
                    _tvShows.value = it.tvShows
                    _combinedContent.value =
                        (it.movies.map { movie -> movie.posterUrl } + it.tvShows.map { tvShow -> tvShow.posterUrl })
                }
            }
        }
    }

    private fun readJsonFromAssets(fileName: String): String {
        return try {
            getApplication<Application>().assets.open(fileName).bufferedReader()
                .use { it.readText() }
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }

    private fun parseJsonToContentData(json: String): TestCases? {
        return try {
            val gson = GsonBuilder()
                .registerTypeAdapter(
                    TestCases::class.java,
                    MoviesSeriesDeserializer(getApplication())
                )
                .create()
            gson.fromJson(json, TestCases::class.java)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            null
        }
    }

    private fun loadCurrentUser() {
        val sharedPref =
            getApplication<Application>().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val email = sharedPref.getString("current_user_email", null)
        if (email != null) {
            val users = readUserData()
            _currentUserLoginData.value = users.find { it.email == email }
        }
    }

    private fun readUserData(): List<UserLoginData> {
        val file = File(getApplication<Application>().filesDir, FILE_NAME)
        return try {
            val jsonString = file.readText()
            if (jsonString.isBlank()) return emptyList()
            val type = object : TypeToken<List<UserLoginData>>() {}.type
            gson.fromJson(jsonString, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    val usernameDisplay: LiveData<String> = _currentUserLoginData.map { user ->
        user?.name ?: "Guest"
    }

    val userHandle: LiveData<String> = _currentUserLoginData.map { user ->
        user?.username ?: "@guest"
    }
}