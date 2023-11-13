package com.example.movieinfomation.repositories

import androidx.lifecycle.MutableLiveData
import com.example.movieinfomation.api.ApiService
import com.example.movieinfomation.models.Movie
import com.example.movieinfomation.models.MovieResponse
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class HomeRepository @Inject constructor(private val apiService: ApiService,
    private val db: FirebaseFirestore) {
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

    suspend fun addWatchedMovie(userId: String, movie: Movie) {
        try {
            val movieMap = mapOf(
                "id" to movie.id,
                "title" to movie.title,
                "posterPath" to movie.posterPath,
                "voteAvg" to movie.voteAverage,
                "releaseDate" to movie.releaseDate,
                "genre" to movie.genre.map {it}
            )
            db.collection("users")
                .document(userId)
                .collection("watchedMovies")
                .document(movie.id.toString())
                .set(movieMap)
                .await()
        } catch (e: Exception) {
            Timber.tag("Exception").d("add user: $e")
        }
    }

    suspend fun getWatchedMovies(userId: String): MutableList<Movie> {
        try {
            val movies = mutableListOf<Movie>()
            val querySnapshot = db.collection("users")
                .document(userId)
                .collection("watchedMovies")
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                for (document in querySnapshot) {
                    val movie = document.toObject(Movie::class.java)
                    movies.add(movie)
                }
            } else {
                Timber.tag("Empty").d("Not list")
            }
            return movies

        } catch (e: Exception) {
            Timber.tag("Exception").d("Get Watched Movie: $e")
        }

        return mutableListOf()
    }

}