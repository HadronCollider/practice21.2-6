package com.makentoshe.androidgithubcitemplate.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CollectionItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCollectionItem(testCard : CollectionItem)

    @Query("SELECT * FROM collectionitem_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<CollectionItem>>
}