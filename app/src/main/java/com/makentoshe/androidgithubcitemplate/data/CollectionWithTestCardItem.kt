package com.makentoshe.androidgithubcitemplate.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collectionwithtestcarditem_table", primaryKeys = ["collectionId", "testCardId"])
data class CollectionWithTestCardItem(
    val collectionId: Int,
    val testCardId: Int
)
