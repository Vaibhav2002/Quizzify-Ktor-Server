package dev.vaibhav.quizzify.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import dev.vaibhav.quizzify.data.dataSource.user.UserDataSource
import dev.vaibhav.quizzify.security.JwtSecrets
import dev.vaibhav.quizzify.security.JwtSecurity
import dev.vaibhav.quizzify.utils.Constants.JWT_AUTH
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import org.koin.ktor.ext.inject

fun Application.configureAuthentication() {
    val jwtSecurity by inject<JwtSecurity>()
    val userDataSource by inject<UserDataSource>()
    install(Authentication) {
        jwt(JWT_AUTH) {
            realm = JwtSecrets.realm
            verifier(
                JWT.require(Algorithm.HMAC256(JwtSecrets.secret))
                    .withIssuer(JwtSecrets.issuer)
                    .build()
            )

            validate { credential ->
                val userDetail = jwtSecurity.getDetailsFromPayload(credential.payload)
                if (userDataSource.doesUserExistById(userDetail.userId))
                    JWTPrincipal(credential.payload)
                else null
            }
        }
    }
}
