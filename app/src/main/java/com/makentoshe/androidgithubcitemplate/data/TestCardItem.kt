package com.makentoshe.androidgithubcitemplate.data

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "testcarditem_table")
data class TestCardItem (
    @PrimaryKey(autoGenerate = true)
    val testCardId : Int,
    val firstText : String,
    val secondText : String
    /*val isInLearning : Boolean,
    val isRemembered : Boolean,
    val score : Float,
    val globalScore : Int*/
)


