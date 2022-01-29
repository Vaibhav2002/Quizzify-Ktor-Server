package dev.vaibhav.quizzify.utils

import dev.vaibhav.quizzify.data.models.response.Response
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

suspend fun ApplicationCall.respondFromResponse(response: Response<*>) {
    when (response) {
        is Response.Success -> respond(status = HttpStatusCode.fromValue(response.status), response)
        is Response.Error -> respond(status = HttpStatusCode.fromValue(response.status), response)
    }
}
