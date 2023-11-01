package com.example.movieinfomation.other

import com.example.movieinfomation.models.Genres

sealed class NetWorkResult<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?): NetWorkResult<T>(data)
    class Error<T>(message: String?, data: T? = null): NetWorkResult<T>(data, message)
    class Loading<T>(): NetWorkResult<T>()
}
