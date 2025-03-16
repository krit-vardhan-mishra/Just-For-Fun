package com.just_for_fun.justforfun

import android.app.Application
import com.just_for_fun.justforfun.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApp)
            modules(listOf(appModule))
        }
    }

}