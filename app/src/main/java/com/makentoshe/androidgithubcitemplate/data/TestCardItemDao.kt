package com.makentoshe.androidgithubcitemplate.data

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface TestCardItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTestCardItem(testCard : TestCardItem)

    @Delete
    suspend fun deleteTestCardItem(testCard : TestCardItem)

    @Update
    suspend fun updateTestCardItem(testCard : TestCardItem)

    @Query("SELECT * FROM testcarditem_table WHERE testCardId = :id")
    fun getById(id: Int): TestCardItem?

    @Query("SELECT * FROM testcarditem_table ORDER BY testCardId ASC")
    fun getTestCardItems(): LiveData<List<TestCardItem>>
}