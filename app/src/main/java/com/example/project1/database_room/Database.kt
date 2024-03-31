package com.example.project1.database_room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class, UserLocationEntity::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun userDao(): UserDAO
}