package dev.vaibhav.quizzify.di

import dev.vaibhav.quizzify.data.dataSource.user.UserDataSource
import dev.vaibhav.quizzify.data.dataSource.user.UserDataSourceImpl
import dev.vaibhav.quizzify.game.Game
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {
    single {
        KMongo.createClient().coroutine.getDatabase("QuizzifyDB")
    }
    single<UserDataSource> {
        UserDataSourceImpl(get())
    }

    single<Game> {
        Game(get())
    }
}
