package com.makentoshe.androidgithubcitemplate.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collectionitem_table")
data class CollectionItem (
    @PrimaryKey(autoGenerate = true)
    var id : Int,
    var color : Int,
    var text : String
)