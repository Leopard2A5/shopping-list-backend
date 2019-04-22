package de.perschon.shoppinglistbackend

import de.perschon.shoppinglistbackend.products.ProductController
import de.perschon.shoppinglistbackend.shoppinglists.ShoppingListController
import de.perschon.shoppinglistbackend.utils.del
import de.perschon.shoppinglistbackend.utils.get
import de.perschon.shoppinglistbackend.utils.post
import io.ktor.application.ApplicationCall
import io.ktor.response.respond
import io.ktor.routing.Routing
import org.koin.ktor.ext.get as inject

fun routes(): Routing.() -> Unit {
    return {
        val products = inject<ProductController>()
        val shoppingLists = inject<ShoppingListController>()

        get("/health", ::health)

        get ("/products", products::getAll)
        post("/products", products::create)
        get ("/products/{id}", products::getById)
        del ("/products/{id}", products::delete)

        get ("/shopping-lists", shoppingLists::getAll)
        post("/shopping-lists", shoppingLists::create)
        get ("/shopping-lists/{id}", shoppingLists::getById)
        del ("/shopping-lists/{id}", shoppingLists::delete)
    }
}

suspend fun health(call: ApplicationCall) {
    call.respond(Health())
}

data class Health(
    val healthy: Boolean = true
)
