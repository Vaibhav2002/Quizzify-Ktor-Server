package dev.vaibhav.quizzify.data.repo.auth

import at.favre.lib.crypto.bcrypt.BCrypt
import dev.vaibhav.quizzify.data.dataSource.user.UserDataSource
import dev.vaibhav.quizzify.data.models.entities.User
import dev.vaibhav.quizzify.data.models.response.Response
import dev.vaibhav.quizzify.utils.Constants.PASSWORD_MISMATCH
import dev.vaibhav.quizzify.utils.Constants.SUCCESS_LOGIN
import dev.vaibhav.quizzify.utils.Constants.SUCCESS_REGISTER
import dev.vaibhav.quizzify.utils.Constants.USER_ALREADY_EXISTS
import dev.vaibhav.quizzify.utils.UserDoesNotExistException

class AuthRepoImpl(private val userDataSource: UserDataSource) : AuthRepo {

    override suspend fun login(email: String, password: String): Response<User> =
        try {
            val user = userDataSource.getUserByEmail(email)
            val doPasswordsMatch = BCrypt.verifyer().verify(password.toCharArray(), user.password)
            if (doPasswordsMatch.verified) Response.Success(data = user, message = SUCCESS_LOGIN)
            else Response.Error(PASSWORD_MISMATCH)
        } catch (e: UserDoesNotExistException) {
            Response.Error(e.message.toString())
        }

    override suspend fun registerUser(username: String, email: String, password: String): Response<User> = try {
        userDataSource.getUserByEmail(email)
        Response.Error(USER_ALREADY_EXISTS)
    } catch (e: UserDoesNotExistException) {
        userDataSource.insertUser(getUserObject(username, email, password))
        val user = userDataSource.getUserByEmail(email)
        Response.Success(data = user, message = SUCCESS_REGISTER)
    }

    private fun getUserObject(username: String, email: String, password: String) = User(
        userName = username,
        email = email,
        password = BCrypt.withDefaults().hashToString(12, password.toCharArray())
    )
}
