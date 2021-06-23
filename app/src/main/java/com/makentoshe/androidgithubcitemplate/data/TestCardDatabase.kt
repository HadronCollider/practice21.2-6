package com.makentoshe.androidgithubcitemplate.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TestCard::class], version = 1, exportSchema = false)
abstract class TestCardDatabase : RoomDatabase() {
    abstract fun testCardDao(): TestCardDao

    companion object {
        @Volatile
        private var INSTANCE: TestCardDatabase? = null

        fun getDatabase(context: Context): TestCardDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TestCardDatabase::class.java,
                    "testcard_database"
                ).build()
                INSTANCE = instance
                return instance
            }

        }
    }
}



























