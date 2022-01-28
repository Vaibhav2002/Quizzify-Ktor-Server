package dev.vaibhav.quizzify.utils

import dev.vaibhav.quizzify.data.models.entities.Option
import dev.vaibhav.quizzify.data.models.entities.Question

object Constants {

    const val SUCCESS_LOGIN = "Successfully Logged In"
    const val SUCCESS_REGISTER = "Successfully Signed up"
    const val PASSWORD_MISMATCH = "Passwords do not match"
    const val USER_ALREADY_EXISTS = "User already exists"
    const val USER_DOES_NOT_EXIST = "User does not exist"

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
