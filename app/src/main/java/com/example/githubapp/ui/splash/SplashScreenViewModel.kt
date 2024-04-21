package com.example.githubapp.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.githubapp.ui.setting.SettingPreferences

class SplashScreenViewModel(private val pref: SettingPreferences) : ViewModel()  {
    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }
}