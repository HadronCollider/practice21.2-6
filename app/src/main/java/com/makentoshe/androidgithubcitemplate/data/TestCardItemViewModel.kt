package com.makentoshe.androidgithubcitemplate.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TestCardItemViewModel(application: Application): AndroidViewModel(application) {
    private var testCardItemDao: TestCardItemDao =
        TestCardItemDatabase.getDatabase(application).testCardItemDao()
    val readAllData: LiveData<List<TestCardItem>> = testCardItemDao.getTestCardItems()

    fun addTestCardItem(testCardItem : TestCardItem) {
        viewModelScope.launch(Dispatchers.IO) {
            testCardItemDao.addTestCardItem(testCardItem)
        }
    }

    fun updateTestCardItem(testCardItem : TestCardItem) {
        viewModelScope.launch(Dispatchers.IO) {
            testCardItemDao.addTestCardItem(testCardItem)
        }
    }

    fun deleteTestCardItem(testCardItem : TestCardItem) {
        viewModelScope.launch(Dispatchers.IO) {
            testCardItemDao.addTestCardItem(testCardItem)
        }
    }
}
