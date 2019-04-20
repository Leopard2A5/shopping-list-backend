package de.perschon.shoppinglistbackend

import de.perschon.shoppinglistbackend.products.ProductController
import io.ktor.application.ApplicationCall
import io.ktor.response.respond
import io.ktor.routing.Routing
import de.perschon.shoppinglistbackend.utils.get
import de.perschon.shoppinglistbackend.utils.post
import org.koin.ktor.ext.get as inject

fun routes(): Routing.() -> Unit {
    return {
        get("/health", ::health)

        run {
            val controller = inject<ProductController>()

            get ("/products", controller::getAllProducts)
            post("/products", controller::create)
        }
    }
}

suspend fun health(call: ApplicationCall) {
    call.respond(Health())
}

data class Health(
    val healthy: Boolean = true
)
