package com.example.movieinfomation.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieinfomation.models.Genre
import com.example.movieinfomation.models.Genres
import com.example.movieinfomation.models.MovieDetail
import com.example.movieinfomation.models.MovieResponse
import com.example.movieinfomation.models.VideoResponse
import com.example.movieinfomation.other.ApiKey
import com.example.movieinfomation.other.NetWorkResult
import com.example.movieinfomation.repositories.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository): ViewModel() {
    var genresResponse = MutableLiveData<NetWorkResult<Genres>>()

    var popularTodayResponse = MutableLiveData<NetWorkResult<MovieResponse>>()

    var moviesWithGenre = MutableLiveData<NetWorkResult<MovieResponse>>()

    var moviesTrending = MutableLiveData<NetWorkResult<MovieResponse>>()

    var moviesNowPlaying = MutableLiveData<NetWorkResult<MovieResponse>>()

    var moviesSearch = MutableLiveData<NetWorkResult<MovieResponse>>()
    var searchPage = 0
    var query = MutableLiveData<String>("")
    var isSearching = MutableLiveData<Boolean>()
    var idToGenre: Map<Int, String>? = null

    var allMovies = MutableLiveData<NetWorkResult<MovieResponse>>()
    var allMoviesPage = 0

    var movieDetail = MutableLiveData<NetWorkResult<MovieDetail>>()

    var moviesSimilar = MutableLiveData<NetWorkResult<MovieResponse>>()

    var movieVideos = MutableLiveData<NetWorkResult<VideoResponse>>()
    var movieTitle = MutableLiveData<String>()
    var listMovieBackStack: MutableList<Int> = mutableListOf()

    init {
        getGenres()
        getPopularToday()
        getMoviesWithGenre()
        getMoviesTrending()
        getMoviesNowPlaying()
    }

    fun popBackStack() {
        if (listMovieBackStack.isNotEmpty()) {
            val pos = listMovieBackStack.size - 1
            listMovieBackStack.removeAt(pos)
        }
    }

    fun getMovieVideos(id: Int) {
        movieVideos.postValue(NetWorkResult.Loading<VideoResponse>())
        try {
            viewModelScope.launch {
                val response = repository.getVideos(id, ApiKey.getApiKey())
                if (response.isSuccessful) {
                    movieVideos.postValue(NetWorkResult.Success<VideoResponse>(response.body()))
                } else {
                    movieVideos.postValue(NetWorkResult.Error<VideoResponse>("Network error!!!"))
                }
            }

        } catch (e: Exception) {
            movieVideos.postValue(NetWorkResult.Error<VideoResponse>(e.message))
        }
    }

    fun getMoviesSimilar(id: Int) {
        moviesSimilar.postValue(NetWorkResult.Loading<MovieResponse>())
        try {
            viewModelScope.launch {
                val response = repository.getMoviesSimilar(id, ApiKey.getApiKey())
                if (response.isSuccessful) {
                    moviesSimilar.postValue(NetWorkResult.Success<MovieResponse>(response.body()))
                } else {
                    moviesSimilar.postValue(NetWorkResult.Error<MovieResponse>("Network error!!!"))
                }
            }

        } catch (e: Exception) {
            moviesSimilar.postValue(NetWorkResult.Error<MovieResponse>(e.message))
        }
    }

    fun getMovieDetail(id: Int) {
        movieDetail.postValue(NetWorkResult.Loading<MovieDetail>())
        try {
            viewModelScope.launch {
                val response = repository.getMovieDetail(id, ApiKey.getApiKey())
                if (response.isSuccessful) {
                    movieDetail.postValue(NetWorkResult.Success<MovieDetail>(response.body()))
                    movieTitle.postValue(response.body()?.title)
                } else {
                    movieDetail.postValue(NetWorkResult.Error<MovieDetail>("Network error!!!"))
                }
            }

        } catch (e: Exception) {
            movieDetail.postValue(NetWorkResult.Error<MovieDetail>(e.message))
        }
    }

    fun getAllMovies() {
        allMoviesPage++
        allMovies.postValue(NetWorkResult.Loading<MovieResponse>())
        try {
            viewModelScope.launch {
                val response = repository.getAllMovies(ApiKey.getApiKey(), allMoviesPage)
                if (response.isSuccessful) {
                    allMovies.postValue(NetWorkResult.Success<MovieResponse>(response.body()))
                } else {
                    allMovies.postValue(NetWorkResult.Error<MovieResponse>("Network error!!!"))
                }
            }

        } catch (e: Exception) {
            allMovies.postValue(NetWorkResult.Error<MovieResponse>(e.message))
        }
    }

    fun getMoviesSearch(query: String) {
        searchPage++
        moviesSearch.postValue(NetWorkResult.Loading<MovieResponse>())
        try {
            viewModelScope.launch {
                val response = repository.getMovieBySearch(ApiKey.getApiKey(), query, searchPage)
                if (response.isSuccessful) {
                    moviesSearch.postValue(NetWorkResult.Success<MovieResponse>(response.body()))
                } else {
                    moviesSearch.postValue(NetWorkResult.Error<MovieResponse>("Network error!!!"))
                }
            }

        } catch (e: Exception) {
            moviesSearch.postValue(NetWorkResult.Error<MovieResponse>(e.message))
        }
    }


    fun getMoviesWithGenre(genre: Int = 27) {
        moviesWithGenre.postValue(NetWorkResult.Loading<MovieResponse>())
        try {
            viewModelScope.launch {
                val response = repository.getMoviesWithGenres(ApiKey.getApiKey(), genre)
                if (response.isSuccessful) {
                    moviesWithGenre.postValue(NetWorkResult.Success<MovieResponse>(response.body()))
                } else {
                    moviesWithGenre.postValue(NetWorkResult.Error<MovieResponse>("Network error!!!"))
                }
            }

        } catch (e: Exception) {
            moviesWithGenre.postValue(NetWorkResult.Error<MovieResponse>(e.message))
        }
    }

    private fun getGenres() {
        genresResponse.postValue(NetWorkResult.Loading<Genres>())
        try {
            viewModelScope.launch {
                val response = repository.getGenres(ApiKey.getApiKey())
                if (response.isSuccessful) {
                    genresResponse.postValue(NetWorkResult.Success<Genres>(response.body()))
                    idToGenre = response.body()!!.genres.associateBy(Genre::id, Genre::name)
                } else {
                    genresResponse.postValue(NetWorkResult.Error<Genres>("Network error!!!"))
                }
            }

        } catch (e: Exception) {
            genresResponse.postValue(NetWorkResult.Error<Genres>(e.message))
        }
    }

    private fun getPopularToday() {
        popularTodayResponse.postValue(NetWorkResult.Loading<MovieResponse>())
        try {
            viewModelScope.launch {
                val response = repository.getPopularMoviesToday(ApiKey.getApiKey(), 1)
                if (response.isSuccessful) {
                    popularTodayResponse.postValue(NetWorkResult.Success<MovieResponse>(response.body()))
                } else {
                    popularTodayResponse.postValue(NetWorkResult.Error<MovieResponse>("Network error!!!"))
                }
            }

        } catch (e: Exception) {
            popularTodayResponse.postValue(NetWorkResult.Error<MovieResponse>(e.message))
        }
    }

    fun getMoviesTrending(timeWindow: String = "day") {
        moviesTrending.postValue(NetWorkResult.Loading<MovieResponse>())
        try {
            viewModelScope.launch {
                val response = repository.getMoviesTrending(timeWindow, ApiKey.getApiKey())
                if (response.isSuccessful) {
                    moviesTrending.postValue(NetWorkResult.Success<MovieResponse>(response.body()))
                } else {
                    moviesTrending.postValue(NetWorkResult.Error<MovieResponse>("Network error!!!"))
                }
            }

        } catch (e: Exception) {
            moviesTrending.postValue(NetWorkResult.Error<MovieResponse>(e.message))
        }
    }

    private fun getMoviesNowPlaying() {
        moviesNowPlaying.postValue(NetWorkResult.Loading<MovieResponse>())
        try {
            viewModelScope.launch {
                val response = repository.getNowPlaying(ApiKey.getApiKey())
                if (response.isSuccessful) {
                    moviesNowPlaying.postValue(NetWorkResult.Success<MovieResponse>(response.body()))
                } else {
                    moviesNowPlaying.postValue(NetWorkResult.Error<MovieResponse>("Network error!!!"))
                }
            }

        } catch (e: Exception) {
            moviesNowPlaying.postValue(NetWorkResult.Error<MovieResponse>(e.message))
        }
    }
}