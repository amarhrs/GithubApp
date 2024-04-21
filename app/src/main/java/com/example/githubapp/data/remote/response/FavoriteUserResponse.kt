package com.example.githubapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class FavoriteUserResponse (

    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("id")
    val id: Int,
)