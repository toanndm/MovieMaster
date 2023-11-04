package com.example.movieinfomation.models

import com.google.gson.annotations.SerializedName

data class Movie (
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("poster_path")
    val posterPath: String = "",
    @SerializedName("genre_ids")
    val genre: List<Int> = mutableListOf(),
    @SerializedName("release_date")
    val releaseDate: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("vote_average")
    val voteAverage: Float = 0f
)