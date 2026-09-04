package com.rola.app.core

sealed class AppException(
    override val message: String,
    override val cause: Throwable? = null,
) : Exception(message, cause) {
    class Authentication(cause: Throwable? = null) : AppException("Authentication failed.", cause)
    class ArCore(message: String, cause: Throwable? = null) : AppException(message, cause)
    class CameraPermission : AppException("Camera permission is required.")
    class MlModel(message: String, cause: Throwable? = null) : AppException(message, cause)
    class Database(cause: Throwable? = null) : AppException("Database operation failed.", cause)
    class Firebase(cause: Throwable? = null) : AppException("Cloud synchronization failed.", cause)
    class Network : AppException("Network is unavailable.")
    class Validation(message: String) : AppException(message)
}
