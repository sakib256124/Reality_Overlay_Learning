package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rola.app.domain.model.User

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val userId: String,
    val name: String,
    val email: String,
    val profileImage: String,
    val isSynced: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis(),
) {
    fun toDomain(): User = User(
        userId = userId,
        name = name,
        email = email,
        profileImage = profileImage,
    )
}

fun User.toEntity(isSynced: Boolean = false): UserEntity = UserEntity(
    userId = userId,
    name = name,
    email = email,
    profileImage = profileImage,
    isSynced = isSynced,
)
