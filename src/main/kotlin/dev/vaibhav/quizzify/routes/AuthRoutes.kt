package dev.vaibhav.quizzify.routes

import dev.vaibhav.quizzify.data.dataSource.user.UserDataSource
import dev.vaibhav.quizzify.data.dataSource.user.UserDataSourceImpl
import dev.vaibhav.quizzify.data.models.entities.User
import dev.vaibhav.quizzify.data.repo.auth.AuthRepo
import dev.vaibhav.quizzify.utils.respondFromResponse
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.toList
import org.litote.kmongo.reactivestreams.KMongo

fun Route.authRoutes() {
    val authRepo by inject<AuthRepo>()
    route("/auth") {
        login(authRepo)
        register(authRepo)
        get("/data") {
            call.respond(KMongo.createClient().listDatabases().toList().toString())
        }
    }
}

fun Route.login(authRepo: AuthRepo) {
    post("/login") {
        val formData = call.receiveParameters()
        val email = formData["email"] ?: ""
        val password = formData["password"] ?: ""
        val response = authRepo.login(email, password)
        call.respond(status = HttpStatusCode.fromValue(response.status), response.getSerialized())
    }
}

fun Route.register(authRepo: AuthRepo) {
    post("/register") {
        val formData = call.receiveParameters()
        val email = formData["email"] ?: ""
        val password = formData["password"] ?: ""
        val username = formData["username"] ?: ""
        val response = authRepo.registerUser(username, email, password)
        call.respond(status = HttpStatusCode.fromValue(response.status), response.getSerialized())
    }
}
