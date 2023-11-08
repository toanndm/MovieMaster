package com.example.movieinfomation.models

import com.google.gson.annotations.SerializedName

data class VideoResponse(
    @SerializedName("results")
    val videos: MutableList<Video>
)
