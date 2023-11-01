package com.example.movieinfomation.models

import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName("id")
    val id: Int = 27,
    @SerializedName("name")
    val name: String = "All"
)
