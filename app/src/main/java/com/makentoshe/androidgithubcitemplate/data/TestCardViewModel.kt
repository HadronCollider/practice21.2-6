package com.makentoshe.androidgithubcitemplate.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TestCardViewModel(application: Application): AndroidViewModel(application) {
    private val readAllData : LiveData<List<TestCard>>
    private val repository: TestCardRepository

    init {
        val testCardDao = TestCardDatabase.getDatabse(application).userDao()
        repository = TestCardRepository(testCardDao)
        readAllData = repository.readAllData
    }

    fun addTestCard(testCard : TestCard) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTestCard(testCard)
        }
    }

}
















