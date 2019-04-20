package de.perschon.shoppinglistbackend

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import io.ktor.application.call
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond

fun exceptionHandling(): StatusPages.Configuration.() -> Unit {
    return {
        exception<MissingKotlinParameterException> { e ->
            call.respond(HttpStatusCode.BadRequest, e.message ?: "missing parameter")
        }
    }
}
