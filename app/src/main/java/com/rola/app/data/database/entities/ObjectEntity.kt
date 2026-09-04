package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rola.app.domain.model.LearningObject
import com.rola.app.domain.model.ObjectModel

@Entity(tableName = "learning_objects")
data class ObjectEntity(
    @PrimaryKey val objectId: String,
    val name: String,
    val category: String,
    val scientificName: String,
    val description: String,
    val uses: List<String>,
    val facts: List<String> = emptyList(),
    val imageUrl: String,
    val isSynced: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis(),
) {
    fun toDomain(): LearningObject = LearningObject(
        objectId = objectId,
        name = name,
        category = category,
        scientificName = scientificName,
        description = description,
        uses = uses,
        facts = facts,
        imageUrl = imageUrl,
    )

    fun toObjectModel(): ObjectModel = ObjectModel(
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

fun LearningObject.toEntity(isSynced: Boolean = false): ObjectEntity = ObjectEntity(
    objectId = objectId,
    name = name,
    category = category,
    scientificName = scientificName,
    description = description,
    uses = uses,
    facts = facts,
    imageUrl = imageUrl,
    isSynced = isSynced,
)
