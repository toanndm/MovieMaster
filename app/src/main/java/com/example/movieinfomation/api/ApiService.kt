package com.example.movieinfomation.api

import com.example.movieinfomation.models.Genres
import com.example.movieinfomation.models.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("genre/movie/list")
    suspend fun getGenres(@Query("api_key") apiKey: String): Response<Genres>

    @GET("movie/popular")
    suspend fun getPopularMoviesToday(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Response<MovieResponse>

    @GET("discover/movie")
    suspend fun getMoviesWithGenres(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genres: Int
    ): Response<MovieResponse>

    @GET("trending/movie/{time_window}")
    suspend fun getMoviesTrending(
        @Path("time_window")
        timeWindow: String,
        @Query("api_key") apiKey: String
    ): Response<MovieResponse>

    @GET("movie/now_playing")
    suspend fun getNowPlaying(
        @Query("api_key")
        apiKey: String
    ): Response<MovieResponse>

}