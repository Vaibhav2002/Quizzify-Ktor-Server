package dev.vaibhav.quizzify.game

import dev.vaibhav.quizzify.data.dataSource.user.UserDataSource
import dev.vaibhav.quizzify.data.models.entities.Question
import dev.vaibhav.quizzify.data.models.entities.User
import dev.vaibhav.quizzify.utils.Constants
import io.ktor.http.cio.websocket.*
import java.util.concurrent.ConcurrentHashMap

class Game(
    private val userDataSource: UserDataSource
) {

    private val players = ConcurrentHashMap<String, Player>()
    private lateinit var questionSet: List<Question>

    fun createGame() {
        questionSet = Constants.sampleQuestions
    }

    fun doesGameAlreadyExist(): Boolean {
        return this::questionSet.isInitialized
    }

    suspend fun joinPlayer(
        sessionId: String,
        userId: String,
        webSocketSession: WebSocketSession
    ) {
        if (players.contains(userId))
            throw Exception()
        players[userId] = Player(userId, sessionId, webSocketSession)
        userDataSource.insertUser(User(userId, "Username", "", 0))
        val user = userDataSource.getUserById(userId)
        val message = "${user.userName} joined the party"
        sendMessageExcept(exceptUserId = "", message)
    }

    suspend fun selectAnswer(
        questionId: String,
        optionId: String,
        playerId: String
    ) {
        val question = questionSet.find { questionId == it.questionId } ?: throw Exception("Question does not exist")
        val questionNo = questionSet.indexOfFirst { it.questionId == questionId } + 1
        val isCorrectAnswer = question.correctOptionId == optionId
        val user = userDataSource.getUserById(playerId)
        if (isCorrectAnswer)
            players[playerId]?.let {
                players[playerId] = it.copy(score = it.score + 1)
            }
        val message = if (isCorrectAnswer) "${user.userName} solved question number $questionNo"
        else "${user.userName} failed to solve question number $questionNo"
        sendMessageExcept(exceptUserId = playerId, message)
    }

    suspend fun sendMessageExcept(
        exceptUserId: String,
        message: String,
    ) {
        players.values.forEach {
            if (it.playerId != exceptUserId)
                it.socket.send(Frame.Text(message))
        }
    }
}
