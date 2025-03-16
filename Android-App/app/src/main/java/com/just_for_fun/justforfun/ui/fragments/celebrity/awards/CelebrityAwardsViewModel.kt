package com.just_for_fun.justforfun.ui.fragments.celebrity.awards

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.just_for_fun.justforfun.data.Awards
import com.just_for_fun.justforfun.util.deserializer.AwardsDeserializer
import java.io.IOException
import java.io.InputStream

class CelebrityAwardsViewModel(application: Application) : AndroidViewModel(application) {

    private val _awardsList = MutableLiveData<List<Awards>>()
    val awardsList: LiveData<List<Awards>> get() = _awardsList

    init {
        loadAwards()
    }

    private fun loadAwards() {
        try {
            val assetManager = getApplication<Application>().assets
            val inputStream: InputStream = assetManager.open("awards.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }

            val gson = GsonBuilder()
                .registerTypeAdapter(Awards::class.java, AwardsDeserializer())
                .create()

            val awardsJsonObject = gson.fromJson(jsonString, JsonObject::class.java)
            val awardsArray = awardsJsonObject.getAsJsonArray("awards")

            val awardsList = awardsArray.map { gson.fromJson(it, Awards::class.java) }
            _awardsList.postValue(awardsList)

        } catch (e: IOException) {
            Log.e("CelebrityAwardsViewModel", "Cannot Read File: ${e.message}")
        } catch (e: Exception) {
            Log.e("CelebrityAwardsViewModel", "Error Parsing JSON: ${e.message}")
        }
    }
}
