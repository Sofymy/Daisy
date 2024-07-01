package com.example.daisy.data.utils

sealed class UiEvent<out T> {
    data object Loading : UiEvent<Nothing>()
    data class Success<out T>(val data: T) : UiEvent<T>()
    data class Error(val message: String) : UiEvent<Nothing>()
}