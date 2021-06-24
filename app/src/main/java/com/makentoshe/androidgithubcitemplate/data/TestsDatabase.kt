package com.makentoshe.androidgithubcitemplate.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CollectionItem::class, TestCardItem::class],
    version = 1, exportSchema = false)
abstract class TestsDatabase : RoomDatabase() {

    abstract fun collectionItemDao(): CollectionItemDao

    abstract fun testCardItemDao(): TestCardItemDao

    companion object {
        @Volatile
        private var INSTANCE: TestsDatabase? = null

        fun getDatabase(context: Context): TestsDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TestsDatabase::class.java,
                    "tests_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
