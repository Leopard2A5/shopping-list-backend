package de.perschon.shoppinglistbackend.graphql

import de.perschon.shoppinglistbackend.products.Product
import de.perschon.shoppinglistbackend.products.ProductService
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.runBlocking
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class Query : KoinComponent {

    private val productService by inject<ProductService>()

    suspend fun products(): List<Product> {
        return productService.getAll()
    }

    suspend fun product(id: String): Product? {
        return productService.getById(id)
    }

}