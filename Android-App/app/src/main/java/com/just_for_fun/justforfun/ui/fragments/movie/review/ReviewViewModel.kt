package com.just_for_fun.justforfun.ui.fragments.movie.review

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.just_for_fun.justforfun.data.Reply
import com.just_for_fun.justforfun.data.Review
import com.just_for_fun.justforfun.util.deserializer.ReplyDeserializer
import com.just_for_fun.justforfun.util.deserializer.ReviewDeserializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReviewViewModel(application: Application) : AndroidViewModel(application) {

    private val _reviews = MutableLiveData<List<Review>?>()
    val reviews: LiveData<List<Review>?> get() = _reviews

    init {
        loadReviewsData()
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
                withContext(Dispatchers.Main) {
                    _reviews.value = reviewsList
                    Log.d("MovieViewModel", "Reviews parsed successfully")
                }
            } catch (e: Exception) {
                Log.e("MovieViewModel", "Error parsing reviews JSON", e)
                _reviews.postValue(null)
            }
        }
    }

}
