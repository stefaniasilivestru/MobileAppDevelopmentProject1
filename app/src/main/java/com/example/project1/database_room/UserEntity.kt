package com.example.project1.database_room

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "users", indices = [Index(value = ["uid"], unique = true)])
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    val username: String,
    val password: String
) {
}