package dev.vaibhav.quizzify.plugins

import dev.vaibhav.quizzify.di.authModule
import dev.vaibhav.quizzify.di.mainModule
import dev.vaibhav.quizzify.di.securityModule
import io.ktor.application.*
import org.koin.ktor.ext.Koin

fun Application.configureDependencyInjection() {
    install(Koin) {
        modules(mainModule, authModule, securityModule)
    }
}
