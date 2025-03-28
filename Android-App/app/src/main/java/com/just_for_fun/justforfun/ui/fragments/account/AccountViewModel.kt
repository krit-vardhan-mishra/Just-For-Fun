package com.just_for_fun.justforfun.ui.fragments.account

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.just_for_fun.justforfun.data.User
import java.io.File

class AccountViewModel(application: Application) : AndroidViewModel(application) {
    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser

    private val FILE_NAME = "user.json"
    private val gson = Gson()

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        val sharedPref = getApplication<Application>().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val email = sharedPref.getString("current_user_email", null)
        if (email != null) {
            val users = readUserData()
            _currentUser.value = users.find { it.email == email }
        }
    }

    private fun readUserData(): List<User> {
        val file = File(getApplication<Application>().filesDir, FILE_NAME)
        return try {
            val jsonString = file.readText()
            if (jsonString.isBlank()) return emptyList()
            val type = object : TypeToken<List<User>>() {}.type
            gson.fromJson(jsonString, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    val usernameDisplay: LiveData<String> = _currentUser.map { user ->
        user?.name ?: "Guest"
    }

    val userHandle: LiveData<String> = _currentUser.map { user ->
        user?.username ?: "@guest"
    }

    fun setCurrentUser(user: User) {
        _currentUser.value = user
    }

    fun clearCurrentUser() {
        _currentUser.value = null
    }

    fun isUserLoggedIn(): Boolean {
        return _currentUser.value != null
    }
}