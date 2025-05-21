package com.example.quince.model

sealed class ResultWrapper<out T> {
    data class Success<out T>(val data: T) : ResultWrapper<T>()
    data class Error(val exception: java.lang.Exception, val message: String? = null) : ResultWrapper<Nothing>()
    object Loading : ResultWrapper<Nothing>()
}