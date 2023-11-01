package com.example.movieinfomation.ui.viewmodels

import android.content.pm.ModuleInfo
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieinfomation.models.Genres
import com.example.movieinfomation.models.MovieResponse
import com.example.movieinfomation.other.ApiKey
import com.example.movieinfomation.other.NetWorkResult
import com.example.movieinfomation.repositories.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository): ViewModel() {
    var genresResponse = MutableLiveData<NetWorkResult<Genres>>()
    var popularTodayResponse = MutableLiveData<NetWorkResult<MovieResponse>>()
    var moviesWithGenre = MutableLiveData<NetWorkResult<MovieResponse>>()

    init {
        getGenres()
        getPopularToday()
        getMoviesWithGenre()
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

}