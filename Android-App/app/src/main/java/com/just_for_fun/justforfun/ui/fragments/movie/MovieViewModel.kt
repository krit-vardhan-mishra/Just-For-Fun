package com.just_for_fun.justforfun.ui.fragments.movie

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class MovieViewModel(application: Application) : AndroidViewModel(application) {
    val typeText = MutableLiveData<String>()
}