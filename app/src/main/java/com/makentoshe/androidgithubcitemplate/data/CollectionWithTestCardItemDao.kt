package com.makentoshe.androidgithubcitemplate.data

import androidx.room.*

@Dao
interface CollectionWithTestCardItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCollectionWithTestCardItem(collectionWithTestCardItem: CollectionWithTestCardItem )

    @Delete
    fun deleteCollectionWithTestCardItem(collectionWithTestCardItem : CollectionWithTestCardItem)

    @Query("SELECT * FROM collectionwithtestcarditem_table WHERE collectionId = :id")
    fun getByCollectionId(id : Int): List<CollectionWithTestCardItem>

    @Query("SELECT * FROM collectionwithtestcarditem_table WHERE testCardId = :id")
    fun getByTestCardId(id : Int): List<CollectionWithTestCardItem>

    @Query("SELECT * FROM collectionwithtestcarditem_table ORDER BY collectionId ASC")
    fun getCollectionWithTestCardItems(): List<CollectionWithTestCardItem>
}