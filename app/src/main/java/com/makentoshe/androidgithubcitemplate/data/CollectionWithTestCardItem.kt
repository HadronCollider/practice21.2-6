package com.makentoshe.androidgithubcitemplate.data

import androidx.room.Embedded
import androidx.room.Relation

data class CollectionWithTestCardItem (
    @Embedded val collectionItem: CollectionItem,
    @Relation(
        parentColumn = "collectionId",
        entityColumn = "collectionCreatorId"
    )
    val testCardsItems: List<TestCardItem>
)
