package de.perschon.shoppinglistbackend

import de.perschon.shoppinglistbackend.products.Product
import de.perschon.shoppinglistbackend.products.ProductController
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.util.pipeline.PipelineContext
import org.koin.ktor.ext.get as inject

fun routes(): Routing.() -> Unit {
    return {
        get("/health", health())

        run {
            val controller = inject<ProductController>()

            get("/products") {
                call.respond(controller.getAllProducts())
            }

            post("/products") {
                val prod = call.receive<Product>()
                call.respond(controller.create(prod))
            }
        }
    }
}

fun health(): suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit {
    return {
        call.respond(Health())
    }
}

data class Health(
    val healthy: Boolean = true
)
