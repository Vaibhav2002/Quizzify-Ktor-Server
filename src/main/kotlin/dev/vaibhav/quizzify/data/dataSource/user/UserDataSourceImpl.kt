package dev.vaibhav.quizzify.data.dataSource.user

import dev.vaibhav.quizzify.data.models.entities.User
import dev.vaibhav.quizzify.utils.Constants
import dev.vaibhav.quizzify.utils.UserDoesNotExistException
import org.bson.conversions.Bson
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class UserDataSourceImpl(db: CoroutineDatabase) : UserDataSource {

    companion object {
        const val USER_COLLECTION = "users"
    }

    private val userCollection = db.getCollection<User>(USER_COLLECTION)

    override suspend fun getUserById(userId: String): User {
        return userCollection.findOneById(userId) ?: throw UserDoesNotExistException()
    }

    override suspend fun getUserByEmail(email: String): User {
        return findUserBy(User::email eq email)
    }

    override suspend fun getUserByUsername(username: String): User {
        return findUserBy(User::userName eq username)
    }

    private suspend fun findUserBy(filter: Bson): User {
        return userCollection.findOne(filter) ?: throw UserDoesNotExistException()
    }

    override suspend fun getAllUsersById(userIds: List<String>): List<User> {
        return userCollection.find().toList().filter { userIds.contains(it.userId) }
    }

    override suspend fun insertUser(user: User) {
        userCollection.insertOne(user)
    }

    override suspend fun doesUserExistById(id: String): Boolean {
        return userCollection.findOneById(id) != null
    }

    override suspend fun doesUserExistByUsername(username: String): Boolean {
        return try {
            getUserByUsername(username)
            true
        } catch (e: UserDoesNotExistException) {
            false
        }
    }

    override suspend fun doesUserExistByEmail(email: String): Boolean {
        return try {
            getUserByEmail(email)
            true
        } catch (e: UserDoesNotExistException) {
            false
        }
    }
}
