package com.example.movieinfomation.models

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("results")
    val movieItems: List<Movie>
)
