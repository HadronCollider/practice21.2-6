package com.makentoshe.androidgithubcitemplate.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CollectionItem::class], version = 1, exportSchema = false)
abstract class CollectionItemDatabase : RoomDatabase() {
    abstract fun collectionItemDao(): CollectionItemDao

    companion object {
        @Volatile
        private var INSTANCE: CollectionItemDatabase? = null

        fun getDatabase(context: Context): CollectionItemDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CollectionItemDatabase::class.java,
                    "collectionitem_database"
                ).build()
                INSTANCE = instance
                return instance
            }

        }
    }
}



























