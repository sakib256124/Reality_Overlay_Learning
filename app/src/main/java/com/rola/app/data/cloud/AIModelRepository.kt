package com.rola.app.data.cloud

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.rola.app.domain.model.AIModelDescriptor
import com.rola.app.domain.model.AIModelUpdatePlan
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

@Singleton
class AIModelRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val cloudStorageRepository: CloudStorageRepository,
) {
    suspend fun getProductionModel(modelFamily: String): AIModelDescriptor? {
        val snapshot = firestore.collection(AI_MODELS_COLLECTION)
            .whereEqualTo("modelFamily", modelFamily)
            .whereEqualTo("status", PRODUCTION_STATUS)
            .orderBy("updatedAt", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()

        return snapshot.documents.firstOrNull()?.data?.toModelDescriptor()
    }

    suspend fun getProductionModelDownloadUrl(modelFamily: String): String? {
        val model = getProductionModel(modelFamily) ?: return null
        return cloudStorageRepository.aiModelUrl(model.modelFamily, model.version)
    }

    suspend fun checkForUpdate(
        modelFamily: String,
        currentModelId: String?,
        appVersionName: String,
    ): AIModelUpdatePlan {
        val production = getProductionModel(modelFamily)
            ?: return AIModelUpdatePlan(
                currentModelId = currentModelId,
                targetModel = null,
                updateAvailable = false,
                reason = "No production model is registered for $modelFamily.",
            )
        val compatible = production.minAppVersion.compareVersionTo(appVersionName) <= 0
        val updateAvailable = compatible && production.modelId != currentModelId
        return AIModelUpdatePlan(
            currentModelId = currentModelId,
            targetModel = production.takeIf { compatible },
            updateAvailable = updateAvailable,
            rollbackModel = getRollbackModel(modelFamily, production.modelId),
            reason = when {
                !compatible -> "Production model requires app ${production.minAppVersion} or newer."
                updateAvailable -> "Model ${production.modelId} is ready for download."
                else -> "Current model is already up to date."
            },
        )
    }

    suspend fun getRollbackModel(
        modelFamily: String,
        excludeModelId: String,
    ): AIModelDescriptor? {
        val snapshot = firestore.collection(AI_MODELS_COLLECTION)
            .whereEqualTo("modelFamily", modelFamily)
            .whereEqualTo("status", PRODUCTION_STATUS)
            .orderBy("updatedAt", Query.Direction.DESCENDING)
            .limit(5)
            .get()
            .await()

        return snapshot.documents
            .mapNotNull { it.data?.toModelDescriptor() }
            .firstOrNull { it.modelId != excludeModelId }
    }

    private fun Map<String, Any>.toModelDescriptor(): AIModelDescriptor = AIModelDescriptor(
        modelId = this["modelId"] as? String ?: "",
        modelFamily = this["modelFamily"] as? String ?: "",
        version = this["version"] as? String ?: "",
        status = this["status"] as? String ?: "",
        accuracy = (this["accuracy"] as? Number)?.toFloat() ?: 0f,
        storagePath = this["storagePath"] as? String ?: "",
        minAppVersion = this["minAppVersion"] as? String ?: "1.0.0",
        updatedAt = (this["updatedAt"] as? Number)?.toLong() ?: 0L,
    )

    private companion object {
        const val AI_MODELS_COLLECTION = "aiModels"
        const val PRODUCTION_STATUS = "production"
    }
}

private fun String.compareVersionTo(other: String): Int {
    val left = split(".").map { it.toIntOrNull() ?: 0 }
    val right = other.split(".").map { it.toIntOrNull() ?: 0 }
    val max = maxOf(left.size, right.size)
    repeat(max) { index ->
        val comparison = (left.getOrElse(index) { 0 }).compareTo(right.getOrElse(index) { 0 })
        if (comparison != 0) return comparison
    }
    return 0
}
