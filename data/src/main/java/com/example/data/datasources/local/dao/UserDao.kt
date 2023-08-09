package com.example.data.datasources.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.models.parcelable.UserInfoParcel

@Dao
interface UserDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserInfoParcel)

    @Delete
    suspend fun delete(user: UserInfoParcel)

    @Query("SELECT * FROM UserInfo where userId = :userId")
    suspend fun getUserById(userId: String): UserInfoParcel?

    @Update
    suspend fun update(user: UserInfoParcel)
}