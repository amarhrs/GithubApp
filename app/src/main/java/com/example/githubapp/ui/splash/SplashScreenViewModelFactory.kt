package com.example.githubapp.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubapp.ui.setting.SettingPreferences

class SplashScreenViewModelFactory (private val pref: SettingPreferences) : ViewModelProvider.NewInstanceFactory()  {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashScreenViewModel::class.java)) {
            return SplashScreenViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}