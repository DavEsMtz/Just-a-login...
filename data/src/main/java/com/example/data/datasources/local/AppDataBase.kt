package com.example.data.datasources.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.datasources.local.dao.UserDao
import com.example.data.models.parcelable.UserInfoParcel

@Database([UserInfoParcel::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract val userDao: UserDao

    companion object {
        const val DB_NAME = "just_a_login.db"
    }

}