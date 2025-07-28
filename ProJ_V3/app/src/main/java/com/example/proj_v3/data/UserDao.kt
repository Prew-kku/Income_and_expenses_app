package com.example.proj_v3.data

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User): Long


    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun getUser(username: String, password: String): User?

    /*@Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: Int): User?*/

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun getUserById(userId: Int): User?


    @Query("SELECT * FROM users ORDER BY id DESC LIMIT 1")
    suspend fun getLatestUser(): User?

    @Query("UPDATE users SET username = :username,password = :password, birthdate = :birthdate, phone = :phone WHERE id = :id")
    suspend fun updateUser(id: Int, username: String,password: String, birthdate: String, phone: String)

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUserById(userId: Int)  // ฟังก์ชันสำหรับลบผู้ใช้


}