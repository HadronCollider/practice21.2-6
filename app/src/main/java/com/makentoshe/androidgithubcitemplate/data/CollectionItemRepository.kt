package com.makentoshe.androidgithubcitemplate.data

import androidx.lifecycle.LiveData

class CollectionItemRepository(private val collectionItemDao: CollectionItemDao) {

    val readAllData: LiveData<List<CollectionItem>> = collectionItemDao.readAllData()

    suspend fun addCollectionItem(collectionItem : CollectionItem) {
        collectionItemDao.addCollectionItem(collectionItem)
    }

}