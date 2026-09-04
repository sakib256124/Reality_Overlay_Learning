package com.rola.app.presentation.state

sealed class UIState<out T> {
    data object Loading : UIState<Nothing>()
    data class Success<T>(val data: T) : UIState<T>()
    data class Error(val message: String, val throwable: Throwable? = null) : UIState<Nothing>()
}
