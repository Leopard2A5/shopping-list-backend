package de.perschon.shoppinglistbackend.config

import io.ktor.features.CallLogging
import io.ktor.features.callIdMdc
import io.ktor.http.HttpHeaders

val callLogging: CallLogging.Configuration.() -> Unit = {
    callIdMdc("callId")
}
