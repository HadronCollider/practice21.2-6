package com.makentoshe.androidgithubcitemplate.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TestCardDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTestCard(testCard : TestCard)

    @Query("SELECT * FROM testcard_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<TestCard>>
}