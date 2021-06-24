package com.makentoshe.androidgithubcitemplate.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TestCardItem::class], version = 1, exportSchema = false)
abstract class TestCardItemDatabase : RoomDatabase() {

    abstract fun testCardItemDao(): TestCardItemDao

    companion object {
        @Volatile
        private var INSTANCE: TestCardItemDatabase? = null

        fun getDatabase(context: Context): TestCardItemDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TestCardItemDatabase::class.java,
                    "testcarditem_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}


