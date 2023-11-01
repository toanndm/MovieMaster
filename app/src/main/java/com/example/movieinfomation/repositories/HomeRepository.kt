package com.example.movieinfomation.repositories

import com.example.movieinfomation.api.ApiService
import com.example.movieinfomation.models.Genre
import javax.inject.Inject

class HomeRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getGenres(apiKey: String) = apiService.getGenres(apiKey)
    suspend fun getPopularMoviesToday(
        apiKey: String,
        pageNumber: Int
    ) = apiService.getPopularMoviesToday(apiKey, pageNumber)

    suspend fun getMoviesWithGenres(
        apiKey: String,
        genre: Int
    ) = apiService.getMoviesWithGenres(apiKey, genre)
}