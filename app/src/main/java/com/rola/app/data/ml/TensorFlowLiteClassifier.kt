package com.rola.app.data.ml

import android.graphics.Bitmap
import com.rola.app.domain.model.RecognitionResult
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.FileNotFoundException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min

@Singleton
class TensorFlowLiteClassifier @Inject constructor(
    private val modelLoader: ModelLoader,
    private val imageProcessor: ImageProcessor,
) {
    private var interpreter: Interpreter? = null
    private var labels: List<String> = emptyList()
    private var inputWidth: Int = DEFAULT_INPUT_SIZE
    private var inputHeight: Int = DEFAULT_INPUT_SIZE
    private var inputDataType: DataType = DataType.FLOAT32

    @Synchronized
    fun ensureModelLoaded() {
        if (interpreter != null) return

        val model = runCatching { modelLoader.loadModel(MODEL_ASSET_PATH) }
            .getOrElse { throwable ->
                val exception = FileNotFoundException(
                    "Missing TensorFlow Lite model at assets/$MODEL_ASSET_PATH",
                )
                exception.initCause(throwable)
                throw exception
            }

        labels = modelLoader.loadLabels(LABELS_ASSET_PATH)
        interpreter = modelLoader.createInterpreter(model).also { loadedInterpreter ->
            val inputTensor = loadedInterpreter.getInputTensor(0)
            val inputShape = inputTensor.shape()
            inputHeight = inputShape.getOrNull(1) ?: DEFAULT_INPUT_SIZE
            inputWidth = inputShape.getOrNull(2) ?: DEFAULT_INPUT_SIZE
            inputDataType = inputTensor.dataType()
        }
    }

    fun classify(bitmap: Bitmap, topK: Int = DEFAULT_TOP_K): List<RecognitionResult> {
        ensureModelLoaded()
        val activeInterpreter = checkNotNull(interpreter) { "TensorFlow Lite interpreter is not loaded." }
        val inputImage = imageProcessor.toModelInput(
            bitmap = bitmap,
            inputWidth = inputWidth,
            inputHeight = inputHeight,
            dataType = inputDataType,
        )

        val outputTensor = activeInterpreter.getOutputTensor(0)
        val outputBuffer = TensorBuffer.createFixedSize(outputTensor.shape(), outputTensor.dataType())
        val outputByteBuffer = outputBuffer.buffer
        outputByteBuffer.rewind()
        activeInterpreter.run(inputImage.buffer, outputByteBuffer)

        return outputBuffer.floatArray
            .asSequence()
            .mapIndexed { index, probability ->
                RecognitionResult(
                    name = labels.getOrElse(index) { "class_$index" },
                    confidence = probability.coerceIn(0f, 1f),
                    timestamp = System.currentTimeMillis(),
                )
            }
            .sortedByDescending { it.confidence }
            .take(min(topK, labels.size.takeIf { it > 0 } ?: topK))
            .filter { it.confidence >= MIN_CONFIDENCE }
            .toList()
    }

    fun close() {
        interpreter?.close()
        interpreter = null
    }

    private companion object {
        const val MODEL_ASSET_PATH = "ml/object_classifier.tflite"
        const val LABELS_ASSET_PATH = "ml/labels.txt"
        const val DEFAULT_INPUT_SIZE = 224
        const val DEFAULT_TOP_K = 3
        const val MIN_CONFIDENCE = 0.45f
    }
}
