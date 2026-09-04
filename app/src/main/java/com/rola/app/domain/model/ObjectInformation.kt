package com.rola.app.domain.model

data class ObjectInformation(
    val objectId: String,
    val name: String,
    val scientificName: String,
    val category: String,
    val description: String,
    val uses: List<String>,
    val facts: List<String>,
    val imageUrl: String,
)

fun ObjectModel.toObjectInformation(): ObjectInformation = ObjectInformation(
    objectId = objectId,
    name = name,
    scientificName = scientificName,
    category = category,
    description = description,
    uses = uses,
    facts = facts,
    imageUrl = imageUrl,
)
