package com.makentoshe.androidgithubcitemplate.data

import androidx.room.*


@Dao
interface TestCardItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addTestCardItem(testCard : TestCardItem) : Long

    @Delete
    fun deleteTestCardItem(testCard : TestCardItem)

    @Update
    fun updateTestCardItem(testCard : TestCardItem)

    @Query("SELECT * FROM testcarditem_table WHERE testCardId = :id")
    fun getById(id: Int): TestCardItem

    @Query("SELECT * FROM testcarditem_table ORDER BY testCardId ASC")
    fun getTestCardItems(): List<TestCardItem>
}