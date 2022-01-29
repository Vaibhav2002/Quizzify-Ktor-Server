package dev.vaibhav.quizzify.data.repo.auth.util

interface PasswordEncryptor {

    fun encryptPassword(password: String): String

    fun verifyPassword(password: String, encryptedPassword: String): Boolean
}
