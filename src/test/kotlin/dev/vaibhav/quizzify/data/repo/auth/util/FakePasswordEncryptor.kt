package dev.vaibhav.quizzify.data.repo.auth.util

class FakePasswordEncryptor : PasswordEncryptor {
    override fun encryptPassword(password: String): String {
        return password.map {
            it.code
        }.joinToString("")
    }

    override fun verifyPassword(password: String, encryptedPassword: String): Boolean {
        return encryptPassword(password) == encryptedPassword
    }
}
