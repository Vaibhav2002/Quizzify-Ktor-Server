package dev.vaibhav.quizzify.data.repo.auth.util

import at.favre.lib.crypto.bcrypt.BCrypt

class BCryptPasswordEncryptor : PasswordEncryptor {

    companion object {
        private val COST = System.getenv()["password_hash_cost"]?.toInt() ?: 0
    }

    override fun encryptPassword(password: String): String {
        return BCrypt.withDefaults().hashToString(COST, password.toCharArray())
    }

    override fun verifyPassword(password: String, encryptedPassword: String): Boolean {
        return BCrypt.verifyer().verify(password.toCharArray(), encryptedPassword).verified
    }
}
