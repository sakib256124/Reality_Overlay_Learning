package com.rola.app.domain.model

data class ObjectModel(
    val objectId: String,
    val name: String,
    val category: String,
    val scientificName: String,
    val description: String,
    val uses: List<String>,
    val facts: List<String> = emptyList(),
    val imageUrl: String,
)
