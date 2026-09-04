package com.rola.app.core

import com.google.ar.core.exceptions.CameraNotAvailableException
import com.google.firebase.FirebaseException
import java.io.FileNotFoundException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorHandler @Inject constructor() {
    fun toUserMessage(throwable: Throwable): String = when (throwable) {
        is AppException -> throwable.message
        is CameraNotAvailableException -> "Camera is unavailable. Close other camera apps and try again."
        is FileNotFoundException -> "Required app model or asset is missing."
        is FirebaseException -> "Cloud service is unavailable. Offline data is still saved."
        is IOException -> "Network is unavailable. Changes will sync later."
        else -> throwable.message ?: "Something went wrong."
    }
}
