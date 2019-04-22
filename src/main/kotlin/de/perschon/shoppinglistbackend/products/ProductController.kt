package de.perschon.shoppinglistbackend.products

import de.perschon.shoppinglistbackend.shoppinglists.ShoppingListService
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond

class ProductController(
    private val service: ProductService,
    private val shoppingListService: ShoppingListService
) {

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

    suspend fun delete(call: ApplicationCall) {
        service.delete(call.parameters["id"]!!)
        call.respond(HttpStatusCode.NoContent)
    }
}
