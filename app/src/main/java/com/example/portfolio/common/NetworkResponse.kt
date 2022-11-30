package com.example.portfolio.common

sealed class NetworkResponse<T>(val data : T? = null, val message: String? = null) {
    class Success<T>(data: T): NetworkResponse<T>(data)
    class Error<T>(data: T? = null, message: String): NetworkResponse<T>(data, message)
    class Loading<T>(data: T? = null): NetworkResponse<T>(data)
}