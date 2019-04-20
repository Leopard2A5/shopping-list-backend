package de.perschon.shoppinglistbackend.products

import org.bson.codecs.pojo.annotations.BsonId
import java.util.*

data class Product(
    @BsonId
    val id: String = UUID.randomUUID().toString(),
    val name: String
)