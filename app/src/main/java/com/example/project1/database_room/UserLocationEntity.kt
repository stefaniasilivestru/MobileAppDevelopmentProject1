package com.example.project1.database_room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "user_locations",
    foreignKeys = [ForeignKey(entity = UserEntity::class,
        parentColumns = ["uid"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE)]
)
data class UserLocationEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    val userId: Int,
    val routeName : String,
    val locationName : String,
    val latitude : Double,
    val longitude : Double
)
