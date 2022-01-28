package dev.vaibhav.quizzify

import dev.vaibhav.quizzify.di.mainModule
import dev.vaibhav.quizzify.plugins.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.ktor.ext.Koin

fun main() {
    embeddedServer(Netty, port = 8082, host = "0.0.0.0") {
        install(Koin) {
            modules(mainModule)
        }
        configureSockets()
        configureRouting()
        configureSerialization()
        configureMonitoring()
        configureSecurity()
    }.start(wait = true)
}
