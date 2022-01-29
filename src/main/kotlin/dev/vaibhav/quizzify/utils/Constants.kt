package dev.vaibhav.quizzify.utils

import dev.vaibhav.quizzify.data.models.entities.Option
import dev.vaibhav.quizzify.data.models.entities.Question

object Constants {

    const val SUCCESS_LOGIN = "Successfully Logged In"
    const val SUCCESS_REGISTER = "Successfully Signed up"
    const val PASSWORD_MISMATCH = "Passwords do not match"
    const val USER_ALREADY_EXISTS = "User already exists"
    const val USERNAME_TAKEN = "Username already in use"
    const val USER_DOES_NOT_EXIST = "User does not exist"
    const val BASE_COLLECTION = "quizzify-data"

    const val JWT_AUTH = "jwt-auth"
    const val JWT_USERNAME = "username"
    const val JWT_USER_ID = "userId"
    const val JWT_EMAIL = "email"

    val sampleQuestions = (0..6).map {
        Question(
            questionId = it.toString(),
            question = "Question $it",
            options = ('a'..'d').map { j ->
                Option(
                    optionId = j.code.toString(),
                    option = j.toString(),
                    isCorrect = j == 'c'
                )
            }
        )
    }
}
