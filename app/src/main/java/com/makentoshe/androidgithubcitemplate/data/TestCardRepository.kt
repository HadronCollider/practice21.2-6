package com.makentoshe.androidgithubcitemplate.data

import androidx.lifecycle.LiveData

class TestCardRepository(private val testCardDao: TestCardDao) {

    val readAllData: LiveData<List<TestCard>> = testCardDao.readAllData()

    suspend fun addTestCard(testCard : TestCard) {
        testCardDao.addTestCard((testCard))
    }

}