package dev.vaibhav.quizzify.plugins

import dev.vaibhav.quizzify.sesssion.GameSession
import io.ktor.application.*
import io.ktor.sessions.*
import io.ktor.util.*

fun Application.configureSecurity() {
    install(Sessions) {
        cookie<GameSession>("SESSION")
    }
    intercept(ApplicationCallPipeline.Features) {
        if (call.sessions.get<GameSession>() == null) {
            val username = call.parameters["username"] ?: "Guest"
            call.sessions.set(
                GameSession(
                    sessionId = generateNonce(),
                    username = username,
                    userId = generateNonce()
                )
            )
        }
    }
}
