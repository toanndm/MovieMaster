package com.example.movieinfomation.models

import com.google.gson.annotations.SerializedName

data class Movie (
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("overview")
    val overview: String = "",
    @SerializedName("poster_path")
    val posterPath: String = "",
    @SerializedName("backdrop_path")
    val backdropPath: String = "",
    @SerializedName("genre_ids")
    val genre: List<Int> = mutableListOf(),
    @SerializedName("release_date")
    val releaseDate: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("vote_average")
    val voteAverage: Float = 0f,
    @SerializedName("vote_count")
    val voteCount: Int = 0
)