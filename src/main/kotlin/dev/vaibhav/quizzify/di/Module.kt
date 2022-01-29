package dev.vaibhav.quizzify.di

import com.mongodb.ConnectionString
import dev.vaibhav.quizzify.data.dataSource.user.UserDataSource
import dev.vaibhav.quizzify.data.dataSource.user.UserDataSourceImpl
import dev.vaibhav.quizzify.data.repo.auth.AuthRepo
import dev.vaibhav.quizzify.data.repo.auth.AuthRepoImpl
import dev.vaibhav.quizzify.data.repo.auth.util.BCryptPasswordEncryptor
import dev.vaibhav.quizzify.data.repo.auth.util.PasswordEncryptor
import dev.vaibhav.quizzify.game.Game
import dev.vaibhav.quizzify.security.JwtSecurity
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {
    single {
        KMongo
            .createClient(ConnectionString(System.getenv("mongo_connection_string")))
            .coroutine.getDatabase("MyCluster")
    }
    single<UserDataSource> {
        UserDataSourceImpl(get())
    }

    single<Game> {
        Game(get())
    }
}

val securityModule = module {
    single<JwtSecurity> {
        JwtSecurity()
    }
}

val authModule = module {
    single<PasswordEncryptor> {
        BCryptPasswordEncryptor()
    }
    single<AuthRepo> {
        AuthRepoImpl(get(), get(), get())
    }
}
