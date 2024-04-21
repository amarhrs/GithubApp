package com.example.githubapp.data.remote.retrofit

import com.example.githubapp.data.remote.response.DetailUserResponse
import com.example.githubapp.data.remote.response.GithubResponse
import com.example.githubapp.data.remote.response.FavoriteUserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun getListGithub(@Query("q") q: String): Call<GithubResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<FavoriteUserResponse>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<FavoriteUserResponse>>
}