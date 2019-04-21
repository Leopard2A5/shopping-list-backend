package de.perschon.shoppinglistbackend.shoppinglists

import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond

class ShoppingListController(private val service: ShoppingListService) {

    suspend fun getAll(call: ApplicationCall) {
        call.respond(service.getAllShoppingLists())
    }

    suspend fun getById(call: ApplicationCall) {
        when (val list = service.getShoppingList(call.parameters["id"]!!)) {
            null -> call.respond(HttpStatusCode.NotFound)
            else -> call.respond(list)
        }
    }

    suspend fun create(call: ApplicationCall) {
        val list = call.receive<ShoppingList>()
        call.respond(service.create(list))
    }

}
