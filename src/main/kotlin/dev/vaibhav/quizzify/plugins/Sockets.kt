package dev.vaibhav.quizzify.plugins

import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.websocket.*
import java.time.*

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
}
