package com.example.movieinfomation.models

import com.google.gson.annotations.SerializedName

data class MovieDetail(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("poster_path")
    val posterPath: String = "",
    @SerializedName("backdrop_path")
    val backdropPath: String = "",
    @SerializedName("genres")
    val genres: List<Genre>? = null,
    @SerializedName("release_date")
    val releaseDate: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("overview")
    val overView: String = "",
    @SerializedName("vote_average")
    val voteAverage: Float = 0f,
    @SerializedName("video")
    val hasVideo: Boolean = false,
    @SerializedName("homepage")
    val homePage: String = "",
    @SerializedName("runtime")
    val runTime: Int = 0
)
