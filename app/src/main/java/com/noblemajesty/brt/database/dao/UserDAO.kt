package com.noblemajesty.brt.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.noblemajesty.brt.database.entities.User

@Dao
interface UserDAO {

    @Query("SELECT * FROM user WHERE email = :userEmail")
    fun findUserByEmail(userEmail: String): User?

    @Insert
    fun createUser(user: User): Long?

    @Update
    fun updateUser(vararg users: User): Int

    @Query("SELECT * FROM user WHERE userId = :id")
    fun getCurrentUser(id: Int): User?
}