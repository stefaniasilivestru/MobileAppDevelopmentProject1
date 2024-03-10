package com.example.project1

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDAO {
    @Insert
    fun insertUser(user: UserEntity)
    @Query("SELECT * FROM users")
    fun getAllUsers(): List<UserEntity>
    @Query("SELECT * FROM users WHERE username = :username")
    fun getUserByUsername(username: String): UserEntity

    @Query("SELECT password FROM users WHERE username = :username")
    fun getPasswordByUsername(username: String): String
}