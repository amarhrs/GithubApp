package com.example.githubapp.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubapp.data.local.entity.FavoriteUser
import com.example.githubapp.repository.FavoriteRepository

class FavoriteViewModel(application: Application) : ViewModel() {
    private val favoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun getFavoriteUser(): LiveData<List<FavoriteUser>> = favoriteRepository.getAllFavorite()
}