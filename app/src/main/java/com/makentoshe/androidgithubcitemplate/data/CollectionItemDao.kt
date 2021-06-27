package com.makentoshe.androidgithubcitemplate.data

import androidx.room.*

@Dao
interface CollectionItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addCollectionItem(collectionItem : CollectionItem)

    @Delete
    fun deleteCollectionItem(collectionItem : CollectionItem)

    @Update
    fun updateCollectionItem(collectionItem : CollectionItem)

    @Query("SELECT * FROM collectionitem_table WHERE collectionId = :id")
    fun getById(id: Int): CollectionItem

    @Query("SELECT * FROM collectionitem_table WHERE isSelected = :flag")
    fun getByIsSelected(flag : Boolean): List<CollectionItem>

    @Query("SELECT * FROM collectionitem_table ORDER BY collectionId ASC")
    fun getCollectionItems(): List<CollectionItem>
}