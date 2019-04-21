package de.perschon.shoppinglistbackend.products

import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond

class ProductController(private val service: ProductService) {

    suspend fun getAll(call: ApplicationCall) {
        call.respond(service.getAll())
    }

    suspend fun getById(call: ApplicationCall) {
        when (val prod = service.getById(call.parameters["id"]!!)) {
            null -> call.respond(HttpStatusCode.NotFound)
            else -> call.respond(prod)
        }
    }

    suspend fun create(call: ApplicationCall) {
        val prod = call.receive<Product>()
        call.respond(service.create(prod))
    }
}
