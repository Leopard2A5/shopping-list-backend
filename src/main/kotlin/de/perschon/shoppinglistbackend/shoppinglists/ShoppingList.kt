package de.perschon.shoppinglistbackend.shoppinglists

import org.bson.codecs.pojo.annotations.BsonId
import java.time.Instant
import java.util.*

data class ShoppingList(
    @BsonId
    val id: String = UUID.randomUUID().toString(),
    val createdAt: Instant = Instant.now(),
    val items: List<ListItem>
)

data class ListItem(
    val productId: String,
    val quantity: Int = 1
)