package de.perschon.shoppinglistbackend.utils

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.routing.Routing
import io.ktor.routing.get as innerGet
import io.ktor.routing.post as innerPost
import io.ktor.routing.delete as innerDelete

fun Routing.get(
    path: String,
    block: suspend (ApplicationCall) -> Unit
) {
    innerGet(path) { block.invoke(call) }
}

fun Routing.post(
    path: String,
    block: suspend (ApplicationCall) -> Unit
) {
    innerPost(path) { block.invoke(call) }
}

fun Routing.del(
    path: String,
    block: suspend (ApplicationCall) -> Unit
) {
    innerDelete(path) { block.invoke(call) }
}
