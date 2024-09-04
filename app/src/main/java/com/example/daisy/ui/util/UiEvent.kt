package com.example.daisy.ui.util

sealed class UiEvent {
    data object Success : UiEvent()
    data class Error(val message: String) : UiEvent()
}