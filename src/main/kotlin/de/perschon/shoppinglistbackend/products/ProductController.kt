package de.perschon.shoppinglistbackend.products

import io.ktor.application.ApplicationCall
import io.ktor.request.receive
import io.ktor.response.respond

class ProductController(private val service: ProductService) {

    suspend fun getAllProducts(call: ApplicationCall) {
        call.respond(service.getAllProducts())
    }

    suspend fun create(call: ApplicationCall) {
        val prod = call.receive<Product>()
        call.respond(service.create(prod))
    }
}
