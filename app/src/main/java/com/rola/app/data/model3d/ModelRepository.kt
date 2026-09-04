package com.rola.app.data.model3d

import com.google.firebase.firestore.FirebaseFirestore
import com.rola.app.domain.model.ModelFileFormat
import com.rola.app.domain.model.ModelRotation
import com.rola.app.domain.model.Object3DModel
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

@Singleton
class ModelRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val modelLoader: ModelLoader,
) {
    suspend fun getModelForObject(objectId: String): Result<ResolvedModelAsset> = runCatching {
        val metadata = fetchRemoteModel(objectId).getOrNull()
            ?: fallbackModels[objectId.normalizedObjectId()]
            ?: Object3DModel(
                modelId = "${objectId.normalizedObjectId()}_default",
                objectId = objectId.normalizedObjectId(),
                modelName = objectId.replaceFirstChar { it.uppercase() },
                modelUrl = "",
                thumbnail = "",
                category = "General Object",
                fileFormat = ModelFileFormat.Glb,
                scale = 0.45f,
                rotation = ModelRotation(),
                description = "Interactive educational 3D visualization.",
            )

        modelLoader.resolveModelAsset(metadata).getOrThrow()
    }

    private suspend fun fetchRemoteModel(objectId: String): Result<Object3DModel> = runCatching {
        firestore.collection(MODELS_COLLECTION)
            .whereEqualTo("objectId", objectId.normalizedObjectId())
            .limit(1)
            .get()
            .await()
            .documents
            .firstOrNull()
            ?.data
            ?.toObject3DModel()
            ?: throw NoSuchElementException("No remote 3D model metadata found.")
    }

    private fun Map<String, Any>.toObject3DModel(): Object3DModel {
        val format = (this["fileFormat"] as? String)
            ?.let { value ->
                runCatching {
                    ModelFileFormat.valueOf(value.lowercase().replaceFirstChar { it.uppercase() })
                }.getOrNull()
            }
            ?: ModelFileFormat.Glb

        return Object3DModel(
            modelId = this["modelId"] as? String ?: this["objectId"] as? String ?: "model",
            objectId = this["objectId"] as? String ?: "unknown",
            modelName = this["modelName"] as? String ?: this["objectId"] as? String ?: "3D Model",
            modelUrl = this["modelUrl"] as? String ?: "",
            thumbnail = this["thumbnail"] as? String ?: "",
            category = this["category"] as? String ?: "",
            fileFormat = format,
            scale = (this["scale"] as? Number)?.toFloat() ?: 0.45f,
            rotation = ModelRotation(
                x = (this["rotationX"] as? Number)?.toFloat() ?: 0f,
                y = (this["rotationY"] as? Number)?.toFloat() ?: 0f,
                z = (this["rotationZ"] as? Number)?.toFloat() ?: 0f,
            ),
            description = this["description"] as? String ?: "",
        )
    }

    private fun String.normalizedObjectId(): String = lowercase()
        .replace(Regex("[^a-z0-9]+"), "_")
        .trim('_')
        .ifBlank { "unknown" }

    private companion object {
        const val MODELS_COLLECTION = "3DModels"

        val fallbackModels = listOf(
            Object3DModel(
                modelId = "bottle_default",
                objectId = "bottle",
                modelName = "Bottle Model",
                modelUrl = "",
                thumbnail = "",
                category = "Plastic Object",
                fileFormat = ModelFileFormat.Glb,
                scale = 0.42f,
                rotation = ModelRotation(),
                description = "A bottle visualization for material and recycling lessons.",
            ),
            Object3DModel(
                modelId = "book_default",
                objectId = "book",
                modelName = "Book Model",
                modelUrl = "",
                thumbnail = "",
                category = "Learning Object",
                fileFormat = ModelFileFormat.Glb,
                scale = 0.36f,
                rotation = ModelRotation(),
                description = "A book visualization for learning media lessons.",
            ),
            Object3DModel(
                modelId = "cup_default",
                objectId = "cup",
                modelName = "Cup Model",
                modelUrl = "",
                thumbnail = "",
                category = "Household Object",
                fileFormat = ModelFileFormat.Glb,
                scale = 0.34f,
                rotation = ModelRotation(),
                description = "A cup visualization for everyday object lessons.",
            ),
        ).associateBy { it.objectId }
    }
}
