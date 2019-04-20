package de.perschon.shoppinglistbackend.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson

val contentNegotiation: ContentNegotiation.Configuration.() -> Unit = {
    jackson {
        registerModule(JavaTimeModule())

        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }
}
