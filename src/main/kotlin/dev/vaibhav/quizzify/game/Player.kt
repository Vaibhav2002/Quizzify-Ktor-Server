package dev.vaibhav.quizzify.game

import io.ktor.http.cio.websocket.*

data class Player(
    val playerId: String,
    val sessionId: String,
    val socket: WebSocketSession,
    val score: Int = 0
)
