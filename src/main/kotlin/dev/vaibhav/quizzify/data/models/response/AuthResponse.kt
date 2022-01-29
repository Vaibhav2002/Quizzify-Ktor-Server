package dev.vaibhav.quizzify.data.models.response

import dev.vaibhav.quizzify.data.models.entities.User
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val user: User,
    val token: String
)
