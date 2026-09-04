package com.rola.app.core

sealed interface ROLAResult<out T> {
    data class Success<T>(val data: T) : ROLAResult<T>
    data class Failure(val exception: AppException) : ROLAResult<Nothing>
}

inline fun <T> runRolaCatching(block: () -> T): ROLAResult<T> =
    try {
        ROLAResult.Success(block())
    } catch (exception: AppException) {
        ROLAResult.Failure(exception)
    } catch (throwable: Throwable) {
        ROLAResult.Failure(AppException.Validation(throwable.message ?: "Unexpected application error."))
    }
