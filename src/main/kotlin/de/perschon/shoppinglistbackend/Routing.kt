package de.perschon.shoppinglistbackend

import de.perschon.shoppinglistbackend.products.ProductController
import de.perschon.shoppinglistbackend.shoppinglists.ShoppingListController
import de.perschon.shoppinglistbackend.utils.del
import de.perschon.shoppinglistbackend.utils.get
import de.perschon.shoppinglistbackend.utils.post
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.post as simplePost
import org.koin.ktor.ext.get as inject

fun routes(): Routing.() -> Unit {
    return {
        val products = inject<ProductController>()
        val shoppingLists = inject<ShoppingListController>()
        val gql = buildGraphQL()

        get("/health", ::health)

        simplePost("/graphql") {
            val query = call.receive<GraphQLRequest>()
            val result = gql.execute(query.query).getData<Any>()
            call.respond(GraphQLResponse(data = result))
        }

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
