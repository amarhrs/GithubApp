package com.example.githubapp.ui.detail

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubapp.data.local.entity.FavoriteUser
import com.example.githubapp.data.remote.response.DetailUserResponse
import com.example.githubapp.data.remote.retrofit.ApiConfig
import com.example.githubapp.repository.FavoriteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailViewModel(application: Application) : ViewModel() {

    private val favoriteRepository: FavoriteRepository = FavoriteRepository(application)

    private val _detailUser = MutableLiveData<DetailUserResponse?>()
    val detailUser: LiveData<DetailUserResponse?> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun detailUser(dataReceived: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(dataReceived)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _detailUser.value = responseBody
                    }
                } else {
                    _errorMessage.value = "Failed to retrieve data: ${response.message()}"
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Network error: ${t.message.toString()}"
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun insertToDb(favoriteUser: FavoriteUser){
        favoriteRepository.insert(favoriteUser)
    }

    fun deleteToDb(favoriteUser: FavoriteUser){
        favoriteRepository.delete(favoriteUser)
    }

    fun favoriteByUsername(username: String): LiveData<FavoriteUser> {
        return favoriteRepository.getByUserName(username)
    }
}