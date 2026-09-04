package com.rola.app.data.cloud

import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

@Singleton
class CloudStorageRepository @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
) {
    suspend fun objectImageUrl(
        objectId: String,
        version: String,
        fileName: String,
    ): String = downloadUrl("objects/$objectId/images/$version/$fileName")

    suspend fun objectModelUrl(
        objectId: String,
        version: String,
        fileName: String,
    ): String = downloadUrl("objects/$objectId/models/$version/$fileName")

    suspend fun aiModelUrl(
        modelFamily: String,
        version: String,
    ): String = downloadUrl("ai-models/$modelFamily/$version/model.tflite")

    suspend fun educationalResourceUrl(
        languageCode: String,
        fileName: String,
    ): String = downloadUrl("education-resources/$languageCode/$fileName")

    private suspend fun downloadUrl(path: String): String =
        firebaseStorage.reference.child(path).downloadUrl.await().toString()
}
