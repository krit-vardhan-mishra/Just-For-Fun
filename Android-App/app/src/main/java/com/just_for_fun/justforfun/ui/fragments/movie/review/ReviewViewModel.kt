package com.just_for_fun.justforfun.ui.fragments.movie.review

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.data.Reply
import com.just_for_fun.justforfun.data.Review
import com.just_for_fun.justforfun.util.deserializer.ReplyDeserializer
import com.just_for_fun.justforfun.util.deserializer.ReviewDeserializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.random.Random

class ReviewViewModel(application: Application) : AndroidViewModel(application) {
    private val _reviews = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>> get() = _reviews

    init {
        loadReviewsData()
    }

    private fun loadReviewsData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("ReviewViewModel", "Starting to load reviews data")
                val assetManager = getApplication<Application>().assets
                val inputStream = assetManager.open("reviews.json")
                val jsonString = inputStream.bufferedReader().use { it.readText() }
                Log.d("ReviewViewModel", "JSON loaded: ${jsonString.take(100)}...") // Log the first 100 chars

                val gson = GsonBuilder()
                    .registerTypeAdapter(Review::class.java, ReviewDeserializer(getApplication()))
                    .registerTypeAdapter(Reply::class.java, ReplyDeserializer(getApplication()))
                    .create()

                val type = object : TypeToken<List<Review>>() {}.type
                val reviewsList: List<Review> = gson.fromJson(jsonString, type)

                Log.d("ReviewViewModel", "Parsed ${reviewsList.size} reviews")

                reviewsList.forEachIndexed { index, review ->
                    Log.d("ReviewViewModel", "Review $index has ${review.replies.size} replies")
                }

                withContext(Dispatchers.Main) {
                    _reviews.value = reviewsList
                    Log.d("ReviewViewModel", "Reviews loaded into LiveData")
                }
            } catch (e: Exception) {
                Log.e("ReviewViewModel", "Error parsing reviews JSON", e)
                _reviews.postValue(emptyList())
            }
        }
    }

    fun postReview(text: String, rating: Float = 4.0f) {
        val currentList = _reviews.value?.toMutableList() ?: mutableListOf()

        val newReview = Review(
            id = Random.nextInt().toString(),
            username = "Current User",
            avatarResId = R.drawable.placeholder_image,
            comment = text,
            date = Date(),
            rating = rating,
            likeCount = 0,
            isLiked = false,
            replies = mutableListOf()
        )

        currentList.add(0, newReview)
        _reviews.value = (_reviews.value?.plus(newReview) ?: listOf(newReview)).toMutableList()
    }
}