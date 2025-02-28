package com.just_for_fun.justforfun.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.just_for_fun.justforfun.ui.fragments.setting.SettingsFragment


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, SettingsFragment())
            .commit()
    }
}
