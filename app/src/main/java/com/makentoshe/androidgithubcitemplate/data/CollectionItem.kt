package com.makentoshe.androidgithubcitemplate.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collectionitem_table")
data class CollectionItem (
    @PrimaryKey(autoGenerate = true)
    val collectionId : Int,
    val color : Int,
    val text : String,
    val isSelected : Boolean
)
