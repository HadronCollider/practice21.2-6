package com.makentoshe.androidgithubcitemplate.data


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CollectionItemViewModel(application: Application): AndroidViewModel(application) {
    private var collectionItemDao: CollectionItemDao =
        TestsDatabase.getDatabase(application).collectionItemDao()
    val readAllData: LiveData<List<CollectionItem>> = collectionItemDao.getCollectionItems()

    fun addCollectionItem(collectionItem : CollectionItem) {
        viewModelScope.launch(Dispatchers.IO) {
            collectionItemDao.addCollectionItem(collectionItem)
        }
    }

    fun updateCollectionItem(collectionItem : CollectionItem) {
        viewModelScope.launch(Dispatchers.IO) {
            collectionItemDao.updateCollectionItem(collectionItem)
        }
    }

    fun deleteCollectionItem(collectionItem : CollectionItem) {
        viewModelScope.launch(Dispatchers.IO) {
            collectionItemDao.deleteCollectionItem(collectionItem)
        }
    }

    fun getByIdCollectionItem(collectionId : Int) : LiveData<CollectionItem> {
        return collectionItemDao.getById(collectionId)
    }
}
