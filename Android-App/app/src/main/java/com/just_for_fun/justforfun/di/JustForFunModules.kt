package com.just_for_fun.justforfun.di

import com.just_for_fun.justforfun.network.AuthApiService
import com.just_for_fun.justforfun.repository.AuthRepository
import com.just_for_fun.justforfun.ui.activities.viewmodel.LoginViewModel
import com.just_for_fun.justforfun.ui.activities.viewmodel.MainViewModel
import com.just_for_fun.justforfun.ui.activities.viewmodel.SignUpViewModel
import com.just_for_fun.justforfun.ui.fragments.account.AccountViewModel
import com.just_for_fun.justforfun.ui.fragments.add.AddViewModel
import com.just_for_fun.justforfun.ui.fragments.celebrity.CelebrityViewModel
import com.just_for_fun.justforfun.ui.fragments.celebrity.awards.CelebrityAwardsViewModel
import com.just_for_fun.justforfun.ui.fragments.celebrity.filmography.CelebrityFilmographyViewModel
import com.just_for_fun.justforfun.ui.fragments.home.HomeViewModel
import com.just_for_fun.justforfun.ui.fragments.movie.MovieViewModel
import com.just_for_fun.justforfun.ui.fragments.movie.movie_or_show.MovieOrShowViewModel
import com.just_for_fun.justforfun.ui.fragments.movie.review.ReviewViewModel
import com.just_for_fun.justforfun.ui.fragments.notification.NotificationViewModel
import com.just_for_fun.justforfun.ui.fragments.poster.PostersViewModel
import com.just_for_fun.justforfun.ui.fragments.search.SearchViewModel
import com.just_for_fun.justforfun.ui.fragments.setting.SettingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.dsl.single
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.jvm.java

val appModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { AddViewModel(get()) }
    viewModel { AccountViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { CelebrityViewModel(get()) }
    viewModel { CelebrityFilmographyViewModel(get()) }
    viewModel { MovieViewModel(get()) }
    viewModel { NotificationViewModel(get()) }
    viewModel { SettingViewModel(get()) }
    viewModel { PostersViewModel(get()) }
    viewModel { CelebrityAwardsViewModel(get()) }
    viewModel { MovieOrShowViewModel(get()) }
    viewModel { ReviewViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { SignUpViewModel(get()) }

    single {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }

    single { AuthRepository(get()) }
}