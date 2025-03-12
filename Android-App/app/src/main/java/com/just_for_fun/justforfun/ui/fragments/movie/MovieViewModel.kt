package com.just_for_fun.justforfun.ui.fragments.movie

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.just_for_fun.justforfun.data.CastCrewMember

class MovieViewModel(application: Application) : AndroidViewModel(application) {
    private val _isTvShow = MutableLiveData<Boolean>(false)
    val isTvShow: LiveData<Boolean> get() = _isTvShow

    private val _title = MutableLiveData<String>("Default Title")
    val title: LiveData<String> get() = _title

    private val _description = MutableLiveData<String>("Default description about the content.")
    val description: LiveData<String> get() = _description

    private val _rating = MutableLiveData<Float>(0f)
    val rating: LiveData<Float> get() = _rating

    private val _totalRating = MutableLiveData<Float>(0f)
    val totalRating: LiveData<Float> get() = _totalRating

    private val _seasons = MutableLiveData<Int>(0)
    val seasons: LiveData<Int> get() = _seasons

    private val _episodes = MutableLiveData<Int>(0)
    val episodes: LiveData<Int> get() = _episodes

    private val _selectedCastMember = MutableLiveData<CastCrewMember?>()
    val selectedCastMember: LiveData<CastCrewMember?> get() = _selectedCastMember

    fun selectCastMember(castMember: CastCrewMember) {
        _selectedCastMember.value = castMember
    }

    fun onCastMemberNavigated() {
        _selectedCastMember.value = null
    }
}