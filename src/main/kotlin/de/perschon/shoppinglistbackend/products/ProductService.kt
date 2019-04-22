package de.perschon.shoppinglistbackend.products

import de.perschon.shoppinglistbackend.shoppinglists.ShoppingListService
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class ProductService(private val shoppingListService: ShoppingListService) : KoinComponent {

    private val collection by lazy {
        inject<CoroutineDatabase>().value.getCollection<Product>()
    }

    suspend fun getAll(): List<Product> = collection.find().toList()

    suspend fun getById(id: String): Product? = collection.findOne(Product::id eq id)

    suspend fun create(prod: Product): Product {
        return prod.also {
            collection.insertOne(it)
        }
    }

    suspend fun delete(id: String) {
        collection.deleteOne(Product::id eq id)
        shoppingListService.deleteProduct(prodId = id)
    }

}
