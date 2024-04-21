package com.example.githubapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubapp.data.local.entity.FavoriteUser
import com.example.githubapp.data.local.room.FavoriteDao
import com.example.githubapp.data.local.room.FavoriteDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mFavoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteDatabase.getDatabase(application)
        mFavoriteDao = db.favoriteDao()
    }

    fun getAllFavorite(): LiveData<List<FavoriteUser>> = mFavoriteDao.getAllFavorite()

    fun insert(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteDao.insert(favoriteUser) }
    }

    fun delete(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteDao.delete(favoriteUser) }
    }

    fun getByUserName(favoriteUser: String): LiveData<FavoriteUser> = mFavoriteDao.getFavoriteUserByUsername(favoriteUser)

}