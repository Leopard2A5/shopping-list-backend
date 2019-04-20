package de.perschon.shoppinglistbackend.products

import org.koin.standalone.KoinComponent
import org.koin.standalone.get
import org.litote.kmongo.coroutine.CoroutineCollection

class ProductController : KoinComponent {

    private val collection by lazy { get<CoroutineCollection<Product>>() }

    suspend fun getAllProducts(): List<Product> = collection.find().toList()

    suspend fun create(prod: Product): Product {
        collection.insertOne(prod)
        return prod
    }
}