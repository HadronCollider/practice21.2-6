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

    fun addTestCard(testCard : CollectionItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCollectionItem(testCard)
        }
    }

}
















