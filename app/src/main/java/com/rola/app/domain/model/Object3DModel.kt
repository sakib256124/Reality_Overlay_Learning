package com.rola.app.domain.model

data class Object3DModel(
    val modelId: String,
    val objectId: String,
    val modelName: String,
    val modelUrl: String,
    val thumbnail: String,
    val category: String,
    val fileFormat: ModelFileFormat,
    val scale: Float,
    val rotation: ModelRotation,
    val description: String,
)

enum class ModelFileFormat(val extension: String) {
    Glb("glb"),
    Gltf("gltf"),
}

data class ModelRotation(
    val x: Float = 0f,
    val y: Float = 0f,
    val z: Float = 0f,
)
