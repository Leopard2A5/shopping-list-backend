package de.perschon.shoppinglistbackend.products

import org.koin.standalone.KoinComponent
import org.koin.standalone.get
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq

class ProductService : KoinComponent {

    private val collection by lazy { get<CoroutineCollection<Product>>() }

    suspend fun getAll(): List<Product> = collection.find().toList()

    suspend fun getById(id: String): Product? = collection.findOne(Product::id eq id)

    suspend fun create(prod: Product): Product {
        return prod.also {
            collection.insertOne(it)
        }
    }

}
