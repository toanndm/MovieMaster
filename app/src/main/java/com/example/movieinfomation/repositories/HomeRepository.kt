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

    suspend fun getMoviesTrending(
        timeWindow: String,
        apiKey: String
    ) = apiService.getMoviesTrending(timeWindow, apiKey)

    suspend fun getNowPlaying(
        apiKey: String
    ) = apiService.getNowPlaying(apiKey)

    suspend fun getMovieBySearch(
        apiKey: String,
        query: String,
        page: Int
    ) = apiService.getMovieBySearch(apiKey, query, page)

    suspend fun getAllMovies(
       apiKey: String,
       page: Int
    ) = apiService.getAllMovies(apiKey, page)

    suspend fun getMovieDetail(
        movieId: Int,
        apiKey: String
    ) = apiService.getMovieDetail(movieId, apiKey)

    suspend fun getMoviesSimilar(
        movieId: Int,
        apiKey: String
    ) = apiService.getMoviesSimilar(movieId, apiKey)

    suspend fun getVideos(
        movieId: Int,
        apiKey: String
    ) = apiService.getVideos(movieId, apiKey)
}