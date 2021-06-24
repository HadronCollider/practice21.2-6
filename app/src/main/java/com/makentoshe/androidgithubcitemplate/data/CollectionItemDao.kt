package com.makentoshe.androidgithubcitemplate.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CollectionItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCollectionItem(testCard : CollectionItem)

    @Delete
    suspend fun deleteCollectionItem(collectionItem : CollectionItem)

    @Update
    suspend fun updateCollectionItem(collectionItem : CollectionItem)

    @Query("SELECT * FROM collectionitem_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<CollectionItem>>
}