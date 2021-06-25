package com.makentoshe.androidgithubcitemplate.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CollectionItemViewModel(application: Application): AndroidViewModel(application) {
    val readAllData : LiveData<List<CollectionItem>>
    private val repository: CollectionItemRepository

    init {
        val testCardDao = CollectionItemDatabase.getDatabase(application).collectionItemDao()
        repository = CollectionItemRepository(testCardDao)
        readAllData = repository.readAllData
    }

    fun addCollectionItem(collectionItem : CollectionItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCollectionItem(collectionItem)
        }
    }

    fun updateCollectionItem(collectionItem : CollectionItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateCollectionItem(collectionItem)
        }
    }

    fun deleteCollectionItem(collectionItem : CollectionItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCollectionItem(collectionItem)
        }
    }

}
















