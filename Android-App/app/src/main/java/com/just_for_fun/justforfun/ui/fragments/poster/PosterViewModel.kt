package com.just_for_fun.justforfun.ui.fragments.poster

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.just_for_fun.justforfun.R

class PostersViewModel(application: Application) : AndroidViewModel(application) {

    private val _posterList = MutableLiveData<List<Int>>()
    val posterList: LiveData<List<Int>> get() = _posterList

    private val _toolbarTitle = MutableLiveData<String>()
    val toolbarTitle: LiveData<String> get() = _toolbarTitle

    private val _toolbarSubtitle = MutableLiveData<String>()
    val toolbarSubtitle: LiveData<String> get() = _toolbarSubtitle

    fun setToolbarData(title: String, subtitle: String) {
        _toolbarTitle.value = title
        _toolbarSubtitle.value = subtitle
    }

    fun loadPosters(dataType: String) {
        try {
            val assetManager = getApplication<Application>().assets
            val inputStream = assetManager.open("sample_cases.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }

            val gson = Gson()
            val jsonObject = gson.fromJson(jsonString, JsonObject::class.java)

            val posterIds = if (dataType == "movies") {
                jsonObject.getAsJsonArray("movies")?.mapNotNull { movieJson ->
                    val movie = gson.fromJson(movieJson, JsonObject::class.java)
                    val posterString = movie.get("posterUrl")?.asString
                    posterString?.let { getResourceIdFromString(it) }
                } ?: emptyList()
            } else {
                jsonObject.getAsJsonArray("tvShows")?.mapNotNull { showJson ->
                    val show = gson.fromJson(showJson, JsonObject::class.java)
                    val posterString = show.get("posterUrl")?.asString
                    posterString?.let { getResourceIdFromString(it) }
                } ?: emptyList()
            }

            _posterList.postValue(posterIds)
        } catch (e: Exception) {
            Log.e("PostersViewModel", "Error loading posters", e)
            _posterList.postValue(emptyList())
        }
    }

    private fun getResourceIdFromString(drawableString: String): Int {
        val resourceName = drawableString.replace("R.drawable.", "")
        val resId = getApplication<Application>().resources.getIdentifier(
            resourceName, "drawable", getApplication<Application>().packageName
        )
        return if (resId == 0) R.drawable.placeholder_image else resId
    }
}