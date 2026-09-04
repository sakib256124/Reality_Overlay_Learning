package com.rola.app.data.model3d

import com.rola.app.domain.model.Object3DModel
import javax.inject.Inject

data class ResolvedModelAsset(
    val model: Object3DModel,
    val sourcePath: String,
    val isCached: Boolean,
)

class ModelLoader @Inject constructor(
    private val assetManager: AssetManager,
) {
    suspend fun resolveModelAsset(model: Object3DModel): Result<ResolvedModelAsset> {
        assetManager.cachedModelPath(model.modelId, model.fileFormat.extension)?.let { path ->
            return Result.success(
                ResolvedModelAsset(model = model, sourcePath = path, isCached = true),
            )
        }

        if (model.modelUrl.startsWith("https://")) {
            assetManager.downloadModel(
                modelId = model.modelId,
                modelUrl = model.modelUrl,
                extension = model.fileFormat.extension,
            ).onSuccess { path ->
                assetManager.trimCache()
                return Result.success(
                    ResolvedModelAsset(model = model, sourcePath = path, isCached = true),
                )
            }
        }

        assetManager.bundledModelPath(model.objectId)?.let { path ->
            return Result.success(
                ResolvedModelAsset(model = model, sourcePath = path, isCached = false),
            )
        }

        return Result.failure(IllegalStateException("No GLB/GLTF model is available for ${model.objectId}."))
    }
}
