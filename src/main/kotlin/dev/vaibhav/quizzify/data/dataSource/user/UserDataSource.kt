package dev.vaibhav.quizzify.data.dataSource.user

import dev.vaibhav.quizzify.data.models.entities.User

interface UserDataSource {

    suspend fun getUserById(userId: String): User

    suspend fun getUserByEmail(email: String): User

    suspend fun getUserByUsername(username: String): User

    suspend fun doesUserExistById(id: String): Boolean

    suspend fun doesUserExistByUsername(username: String): Boolean

    suspend fun doesUserExistByEmail(email: String): Boolean

    suspend fun getAllUsersById(userIds: List<String>): List<User>

    suspend fun insertUser(user: User)
}
