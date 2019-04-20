package de.perschon.shoppinglistbackend.products

import io.ktor.application.ApplicationCall
import io.ktor.request.receive
import io.ktor.response.respond
import org.koin.standalone.KoinComponent
import org.koin.standalone.get
import org.litote.kmongo.coroutine.CoroutineCollection

class ProductController : KoinComponent {

    private val collection by lazy { get<CoroutineCollection<Product>>() }

    suspend fun getAllProducts(call: ApplicationCall) {
        call.respond(collection.find().toList())
    }

    suspend fun create(call: ApplicationCall) {
        val prod = call.receive<Product>()
        collection.insertOne(prod)
        call.respond(prod)
    }
}
