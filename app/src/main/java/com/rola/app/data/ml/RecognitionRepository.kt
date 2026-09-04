package com.rola.app.data.ml

import android.graphics.Bitmap
import com.google.ar.core.Frame
import com.rola.app.domain.model.RecognitionResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

sealed class RecognitionOutcome {
    data class Success(val results: List<RecognitionResult>) : RecognitionOutcome()
    data class LowConfidence(val bestResult: RecognitionResult?) : RecognitionOutcome()
    data class Error(val message: String, val throwable: Throwable? = null) : RecognitionOutcome()
}

@Singleton
class RecognitionRepository @Inject constructor(
    private val classifier: TensorFlowLiteClassifier,
    private val imageProcessor: ImageProcessor,
) {
    fun copyFrameToBitmap(frame: Frame): Bitmap? = imageProcessor.frameToBitmap(frame)

    suspend fun recognizeBitmap(
        bitmap: Bitmap,
        dispatcher: CoroutineDispatcher = Dispatchers.Default,
    ): RecognitionOutcome = withContext(dispatcher) {
        runCatching { classifier.classify(bitmap) }
            .fold(
                onSuccess = { results ->
                    if (results.isNotEmpty()) {
                        RecognitionOutcome.Success(results)
                    } else {
                        RecognitionOutcome.LowConfidence(bestResult = null)
                    }
                },
                onFailure = { throwable ->
                    RecognitionOutcome.Error(
                        message = throwable.message ?: "TensorFlow Lite inference failed.",
                        throwable = throwable,
                    )
                },
            )
    }

    fun release() {
        classifier.close()
    }
}
