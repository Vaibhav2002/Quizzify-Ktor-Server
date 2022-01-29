package dev.vaibhav.quizzify

import dev.vaibhav.quizzify.plugins.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureDependencyInjection()
        configureAuthentication()
        configureSockets()
        configureRouting()
        configureSerialization()
        configureMonitoring()
        configureSessions()
    }.start(wait = true)
}
