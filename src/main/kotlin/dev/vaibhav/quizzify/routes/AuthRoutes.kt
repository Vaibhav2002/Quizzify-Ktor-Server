package dev.vaibhav.quizzify.routes

import dev.vaibhav.quizzify.data.dataSource.user.UserDataSource
import dev.vaibhav.quizzify.data.models.entities.User
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.routing.*

fun Route.authRoutes() {
    route("/auth") {
    }
}

fun Route.login(userDataSource: UserDataSource) {
    post("/login") {
        val formData = call.receiveParameters()
        val username = formData["username"]
        val password = formData["password"]
    }
}
