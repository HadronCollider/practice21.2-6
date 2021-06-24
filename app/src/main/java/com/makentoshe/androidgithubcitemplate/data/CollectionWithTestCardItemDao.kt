package com.makentoshe.androidgithubcitemplate.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CollectionWithTestCardItemDao {
    @Transaction
    @Query("SELECT * FROM collectionitem_table")
    fun getCollectionWithTestCardItems(): List<CollectionWithTestCardItem>
}