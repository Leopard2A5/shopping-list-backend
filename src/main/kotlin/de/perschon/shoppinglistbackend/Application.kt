package de.perschon.shoppinglistbackend

import de.perschon.shoppinglistbackend.config.*
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.*
import io.ktor.routing.routing
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.standalone.StandAloneContext.startKoin

fun main() {
    val config = loadConfig()

    val env = applicationEngineEnvironment {
        this.config = KonfKtorConfig(config = config)
        connector {
            host = config[Ktor.Deployment.host]
            port = config[Ktor.Deployment.port]
        }
    }

    initDependencyInjection(config)

    embeddedServer(Netty, env).start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation, contentNegotiation)
    install(CallId, callId)
    install(CallLogging, callLogging)
    install(Compression)
    install(StatusPages, exceptionHandling())

    routing(routes())
}
