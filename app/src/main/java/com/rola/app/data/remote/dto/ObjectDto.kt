package com.rola.app.data.remote.dto

import com.rola.app.domain.model.ObjectModel

data class ObjectDto(
    val objectId: String,
    val name: String,
    val category: String,
    val scientificName: String,
    val description: String,
    val uses: List<String>,
    val facts: List<String> = emptyList(),
    val imageUrl: String,
) {
    fun toDomain(): ObjectModel = ObjectModel(
        objectId = objectId,
        name = name,
        category = category,
        scientificName = scientificName,
        description = description,
        uses = uses,
        facts = facts,
        imageUrl = imageUrl,
    )
}
