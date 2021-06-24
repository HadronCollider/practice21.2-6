package com.makentoshe.androidgithubcitemplate.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CollectionItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCollectionItem(collectionItem : CollectionItem)

    @Delete
    suspend fun deleteCollectionItem(collectionItem : CollectionItem)

    @Update
    suspend fun updateCollectionItem(collectionItem : CollectionItem)

    @Query("SELECT * FROM collectionitem_table WHERE collectionId = :id")
    fun getById(id: Int): LiveData<CollectionItem>

    @Query("SELECT * FROM collectionitem_table ORDER BY collectionId ASC")
    fun getCollectionItems(): LiveData<List<CollectionItem>>
}