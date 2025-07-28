package com.example.proj_v3.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class, Transaction::class, Transaction2::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun transactionDao(): TransactionDao
    abstract fun transaction2Dao(): Transaction2Dao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "transactions_db"
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration() // ใช้เมื่อเปลี่ยน version
                    .build()
            }
            return INSTANCE!!
        }
    }
}
