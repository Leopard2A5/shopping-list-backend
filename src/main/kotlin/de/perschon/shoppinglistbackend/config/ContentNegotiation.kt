package de.perschon.shoppinglistbackend.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson

val contentNegotiation: ContentNegotiation.Configuration.() -> Unit = {
    jackson {
        configureObjectMapper(this)
    }
}

fun configureObjectMapper(om: ObjectMapper): ObjectMapper {
    return om.apply {
        registerModule(JavaTimeModule())

        enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }
}

fun objectMapper(): ObjectMapper {
    return configureObjectMapper(jacksonObjectMapper())
}
