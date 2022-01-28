package dev.vaibhav.quizzify.data.repo.auth

import dev.vaibhav.quizzify.data.models.entities.User
import dev.vaibhav.quizzify.data.models.response.Response

interface AuthRepo {
    suspend fun login(email: String, password: String): Response<User>
    suspend fun registerUser(username: String, email: String, password: String): Response<User>
}
