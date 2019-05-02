package de.perschon.shoppinglistbackend.graphql

import de.perschon.shoppinglistbackend.products.Product
import de.perschon.shoppinglistbackend.products.ProductService
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class Mutation :KoinComponent {

    private val productService by inject<ProductService>()

    suspend fun createProduct(product: NewProductInput): Product {
        return productService.create(
            Product(
                name = product.name
            )
        )
    }

}

data class NewProductInput(
    val name: String
)