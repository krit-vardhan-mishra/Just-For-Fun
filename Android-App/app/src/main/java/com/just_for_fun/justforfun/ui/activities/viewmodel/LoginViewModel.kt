package com.just_for_fun.justforfun.ui.activities.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.just_for_fun.justforfun.repository.AuthRepository
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoginViewModel(application: Application) : AndroidViewModel(application), KoinComponent {
    private val authRepository: AuthRepository by inject()

    private val _loginStatus = MutableLiveData<Result<String>>()
    val loginStatus: LiveData<Result<String>> = _loginStatus

    private val _signupStatus = MutableLiveData<Result<String>>()
    val signupStatus: LiveData<Result<String>> = _signupStatus

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.login(email, password)
            _loginStatus.postValue(result)
        }
    }

    fun signUp(name: String, email: String, username: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.signUp(name, email, username, password)
            _signupStatus.postValue(result)
        }
    }
}
