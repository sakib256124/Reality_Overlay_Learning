package com.rola.app.data.ml

import android.content.Context
import android.content.res.AssetFileDescriptor
import dagger.hilt.android.qualifiers.ApplicationContext
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModelLoader @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun loadModel(modelAssetPath: String): MappedByteBuffer {
        val descriptor: AssetFileDescriptor = context.assets.openFd(modelAssetPath)
        FileInputStream(descriptor.fileDescriptor).use { inputStream ->
            return inputStream.channel.map(
                FileChannel.MapMode.READ_ONLY,
                descriptor.startOffset,
                descriptor.declaredLength,
            )
        }
    }

    fun loadLabels(labelsAssetPath: String): List<String> =
        context.assets.open(labelsAssetPath).bufferedReader().useLines { lines ->
            lines.map { it.trim() }
                .filter { it.isNotEmpty() }
                .toList()
        }

    fun createInterpreter(model: MappedByteBuffer): Interpreter {
        val options = Interpreter.Options()
            .setNumThreads(DEFAULT_THREAD_COUNT)
            .setUseXNNPACK(true)
        return Interpreter(model, options)
    }

    private companion object {
        const val DEFAULT_THREAD_COUNT = 2
    }
}
