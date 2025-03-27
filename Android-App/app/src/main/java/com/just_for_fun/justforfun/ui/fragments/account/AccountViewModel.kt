package com.just_for_fun.justforfun.ui.fragments.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.just_for_fun.justforfun.data.User

class AccountViewModel(application: Application) : AndroidViewModel(application) {
    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser

    val usernameDisplay: LiveData<String> = Transformations.map(_currentUser) { user ->
        user?.name ?: "Guest"
    }
    val userHandle: LiveData<String> = Transformations.map(_currentUser) { user ->
        user?.username ?: "@guest"
    }

    fun setCurrentUser(user: User) {
        _currentUser.postValue(user)
    }

    fun clearCurrentUser() {
        _currentUser.postValue(null)
    }

    fun isUserLoggedIn(): Boolean {
        return _currentUser.value != null
    }
}