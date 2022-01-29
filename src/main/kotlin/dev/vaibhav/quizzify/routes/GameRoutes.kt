package dev.vaibhav.quizzify.routes

import dev.vaibhav.quizzify.data.models.response.Response
import dev.vaibhav.quizzify.game.Game
import dev.vaibhav.quizzify.sesssion.GameSession
import dev.vaibhav.quizzify.utils.Constants.JWT_AUTH
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlin.Exception

fun Route.createGameRoute(game: Game) {
    authenticate(JWT_AUTH) {
        post("/create") {
            if (game.doesGameAlreadyExist())
                call.respond(status = HttpStatusCode.BadRequest, "Game already exists")
            game.createGame()
            call.respond(Response.Success<Unit>(message = "Game created successfully").getSerialized())
        }
    }
}

fun Route.joinGameRoute(game: Game) {
    webSocket("/join-game") {
        val session = call.sessions.get<GameSession>()
        session?.let {
            try {
                game.joinPlayer(
                    sessionId = session.sessionId,
                    userId = session.userId,
                    webSocketSession = this
                )
            } catch (e: Exception) {
                call.respond(status = HttpStatusCode.BadRequest, e.message.toString())
            }
            incoming.consumeEach { frame ->
                if (frame is Frame.Text)
                    game.sendMessageExcept("", frame.readText())
            }
        }
    }
}
