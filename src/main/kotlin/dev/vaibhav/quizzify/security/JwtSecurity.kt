package dev.vaibhav.quizzify.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.Payload
import dev.vaibhav.quizzify.data.models.entities.User
import dev.vaibhav.quizzify.utils.Constants.JWT_EMAIL
import dev.vaibhav.quizzify.utils.Constants.JWT_USERNAME
import dev.vaibhav.quizzify.utils.Constants.JWT_USER_ID

data class UserDetails(
    val email: String,
    val userId: String,
    val username: String
)

class JwtSecurity {

    companion object {
        private val secret = JwtSecrets.secret
        private val issuer = JwtSecrets.issuer
        private val audience = JwtSecrets.audience
        private val myRealm = JwtSecrets.realm
    }

    fun getToken(user: User): String {
        val token = JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim(JWT_USERNAME, user.userName)
            .withClaim(JWT_EMAIL, user.email)
            .withClaim(JWT_USER_ID, user.userId)
            .sign(Algorithm.HMAC256(secret))
        return token.toString()
    }

    fun getDetailsFromPayload(payload: Payload): UserDetails {
        return UserDetails(
            userId = payload.getClaim(JWT_USER_ID).asString(),
            email = payload.getClaim(JWT_EMAIL).asString(),
            username = payload.getClaim(JWT_USERNAME).asString()
        )
    }
}
