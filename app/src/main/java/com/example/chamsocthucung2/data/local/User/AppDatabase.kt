package com.example.chamsocthucung2.data.local


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.chamsocthucung2.data.model.user.ProfileUserDao
import com.example.chamsocthucung2.data.model.user.ProfileUserEntity


@Database(entities = [ProfileUserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun profileUserDao(): ProfileUserDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null


        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
