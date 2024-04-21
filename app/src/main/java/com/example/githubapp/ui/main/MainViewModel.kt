package com.example.githubapp.ui.main

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.githubapp.data.remote.response.GithubResponse
import com.example.githubapp.data.remote.response.ItemItems
import com.example.githubapp.data.remote.retrofit.ApiConfig
import com.example.githubapp.ui.setting.SettingPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {

    private val _listGithub = MutableLiveData<List<ItemItems>>()
    val listGithub: LiveData<List<ItemItems>> = _listGithub

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    init {
        getGithubUser(GITHUB_LOGIN)
    }

    fun getGithubUser(search: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getListGithub(search)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listGithub.value = responseBody.items
                    }
                } else {
                    _errorMessage.value = "Failed to retrieve data: ${response.message()}"
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Network error: ${t.message.toString()}"
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    companion object {
        private const val GITHUB_LOGIN = "arif"
    }

}