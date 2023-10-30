package com.example.movieinfomation.other

object ApiKey {
    init {
        System.loadLibrary("native-lib")
    }
    external fun getApiKey(): String
}