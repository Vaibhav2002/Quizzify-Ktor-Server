package dev.vaibhav.quizzify.data.repo.auth

import dev.vaibhav.quizzify.data.dataSource.user.UserDataSource
import dev.vaibhav.quizzify.data.models.entities.User
import dev.vaibhav.quizzify.data.models.response.AuthResponse
import dev.vaibhav.quizzify.data.models.response.Response
import dev.vaibhav.quizzify.data.repo.auth.util.PasswordEncryptor
import dev.vaibhav.quizzify.security.JwtSecurity
import dev.vaibhav.quizzify.utils.Constants.PASSWORD_MISMATCH
import dev.vaibhav.quizzify.utils.Constants.SUCCESS_LOGIN
import dev.vaibhav.quizzify.utils.Constants.SUCCESS_REGISTER
import dev.vaibhav.quizzify.utils.Constants.USERNAME_TAKEN
import dev.vaibhav.quizzify.utils.Constants.USER_ALREADY_EXISTS
import dev.vaibhav.quizzify.utils.UserDoesNotExistException

class AuthRepoImpl(
    private val userDataSource: UserDataSource,
    private val passwordEncryptor: PasswordEncryptor,
    private val security: JwtSecurity
) : AuthRepo {

    override suspend fun login(email: String, password: String): Response<AuthResponse> =
        try {
            val user = userDataSource.getUserByEmail(email)
            val doPasswordsMatch = passwordEncryptor.verifyPassword(password, user.password)
            if (doPasswordsMatch) {
                Response.Success(data = getAuthResponse(user), message = SUCCESS_LOGIN)
            } else Response.Error(PASSWORD_MISMATCH)
        } catch (e: UserDoesNotExistException) {
            Response.Error(e.message.toString())
        }

    override suspend fun registerUser(username: String, email: String, password: String): Response<AuthResponse> {
        return when {
            userDataSource.doesUserExistByEmail(email) -> Response.Error(USER_ALREADY_EXISTS)
            userDataSource.doesUserExistByUsername(username) -> Response.Error(USERNAME_TAKEN)
            else -> {
                val user = saveUserInDbAndReturnUser(getUserObject(username, email, password))
                Response.Success(data = getAuthResponse(user), message = SUCCESS_REGISTER)
            }
        }
    }

    private fun getAuthResponse(user: User) = AuthResponse(
        user = user,
        token = security.getToken(user)
    )

    private suspend fun saveUserInDbAndReturnUser(user: User): User {
        userDataSource.insertUser(user)
        return userDataSource.getUserByEmail(user.email)
    }

    private fun getUserObject(username: String, email: String, password: String) = User(
        userName = username,
        email = email,
        password = passwordEncryptor.encryptPassword(password)
    )
}
