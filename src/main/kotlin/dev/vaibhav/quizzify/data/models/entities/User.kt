package dev.vaibhav.quizzify.data.models.entities

import kotlinx.serialization.Transient
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class User(
    val userName: String,
    val email: String,
    @Transient
    val password: String,
    val gamesWon: Int = 0,
    @BsonId
    val userId: String = ObjectId().toString(),
)
