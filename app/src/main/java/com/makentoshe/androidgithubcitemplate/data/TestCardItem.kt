package com.makentoshe.androidgithubcitemplate.data

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "testcarditem_table")
data class TestCardItem (
    @PrimaryKey(autoGenerate = true)
    var testCardId : Int,
    var firstText : String,
    var secondText : String,
    var score : Float,
    var globalScore : Int
)


