package com.rola.app.data.model3d

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class AssetManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val modelCacheDir: File by lazy {
        File(context.filesDir, MODEL_CACHE_DIR).apply { mkdirs() }
    }

    fun bundledModelPath(objectId: String): String? {
        val candidate = "models/${objectId.normalizedObjectId()}.glb"
        return runCatching {
            context.assets.open(candidate).close()
            candidate
        }.getOrNull()
    }

    fun cachedModelPath(modelId: String, extension: String): String? {
        val file = File(modelCacheDir, "${modelId.normalizedObjectId()}.$extension")
        return file.takeIf { it.exists() && it.length() > 0L }?.absolutePath
    }

    suspend fun downloadModel(
        modelId: String,
        modelUrl: String,
        extension: String,
    ): Result<String> = withContext(Dispatchers.IO) {
        runCatching {
            require(modelUrl.startsWith("https://")) { "Only HTTPS model URLs are supported." }
            val output = File(modelCacheDir, "${modelId.normalizedObjectId()}.$extension")
            val connection = URL(modelUrl).openConnection() as HttpURLConnection
            connection.connectTimeout = CONNECT_TIMEOUT_MS
            connection.readTimeout = READ_TIMEOUT_MS
            connection.instanceFollowRedirects = true

            connection.inputStream.use { input ->
                output.outputStream().use { outputStream ->
                    input.copyTo(outputStream)
                }
            }

            require(output.length() <= MAX_MODEL_SIZE_BYTES) { "3D model exceeds cache size limit." }
            output.absolutePath
        }
    }

    suspend fun trimCache(maxBytes: Long = MAX_CACHE_BYTES) = withContext(Dispatchers.IO) {
        val files = modelCacheDir.listFiles().orEmpty()
            .sortedByDescending { it.lastModified() }
        var total = files.sumOf { it.length() }
        files.asReversed().forEach { file ->
            if (total <= maxBytes) return@forEach
            total -= file.length()
            file.delete()
        }
    }

    private fun String.normalizedObjectId(): String = lowercase()
        .replace(Regex("[^a-z0-9]+"), "_")
        .trim('_')
        .ifBlank { "model" }

    private companion object {
        const val MODEL_CACHE_DIR = "model3d_cache"
        const val CONNECT_TIMEOUT_MS = 8_000
        const val READ_TIMEOUT_MS = 15_000
        const val MAX_MODEL_SIZE_BYTES = 18L * 1024L * 1024L
        const val MAX_CACHE_BYTES = 96L * 1024L * 1024L
    }
}
