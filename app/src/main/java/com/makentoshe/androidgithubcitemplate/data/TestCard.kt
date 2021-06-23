package com.makentoshe.androidgithubcitemplate.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "testcard_table")
data class TestCard (
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val value2 : String,
    val value3 : String
)