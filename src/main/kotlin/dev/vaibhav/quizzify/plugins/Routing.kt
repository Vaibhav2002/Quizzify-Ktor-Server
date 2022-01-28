package dev.vaibhav.quizzify.plugins

import dev.vaibhav.quizzify.game.Game
import dev.vaibhav.quizzify.routes.createGameRoute
import dev.vaibhav.quizzify.routes.joinGameRoute
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val game by inject<Game>()
    install(Routing) {
        get("/") {
            call.respond("Hey ya!")
        }
        createGameRoute(game)
        joinGameRoute(game)
    }
}
