package dev.vaibhav.quizzify.data.repo.auth

import at.favre.lib.crypto.bcrypt.BCrypt
import com.google.common.truth.Truth.assertThat
import dev.vaibhav.quizzify.data.dataSource.user.UserDataSource
import dev.vaibhav.quizzify.data.models.entities.User
import dev.vaibhav.quizzify.data.models.response.Response
import dev.vaibhav.quizzify.data.repo.auth.util.FakePasswordEncryptor
import dev.vaibhav.quizzify.data.repo.auth.util.PasswordEncryptor
import dev.vaibhav.quizzify.security.JwtSecurity
import dev.vaibhav.quizzify.utils.Constants.PASSWORD_MISMATCH
import dev.vaibhav.quizzify.utils.Constants.SUCCESS_LOGIN
import dev.vaibhav.quizzify.utils.Constants.SUCCESS_REGISTER
import dev.vaibhav.quizzify.utils.Constants.USERNAME_TAKEN
import dev.vaibhav.quizzify.utils.Constants.USER_ALREADY_EXISTS
import dev.vaibhav.quizzify.utils.Constants.USER_DOES_NOT_EXIST
import dev.vaibhav.quizzify.utils.UserDoesNotExistException
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class AuthRepoImplTest {

    @MockK
    lateinit var userDataSource: UserDataSource

    private lateinit var authRepo: AuthRepoImpl
    private lateinit var passwordEncryptor: PasswordEncryptor
    private lateinit var jwtSecurity: JwtSecurity

    private val email = "test@test.com"
    private val username = "testUser"
    private val password = "TestUser123"
    private lateinit var user: User

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        passwordEncryptor = FakePasswordEncryptor()
        jwtSecurity = JwtSecurity()
        authRepo = AuthRepoImpl(userDataSource, passwordEncryptor, jwtSecurity)
        user = User(
            userName = username,
            email = email,
            password = passwordEncryptor.encryptPassword(password)
        )
    }

    @Test
    fun `login is invoked but user is not present, return Resource Error with correct message`() = runTest {
        // given
        coEvery { userDataSource.getUserByEmail(email) } throws UserDoesNotExistException()

        // when
        val response = authRepo.login(email, password)
        println(response)

        // then
        assertThat(response).isInstanceOf(Response.Error::class.java)
        assertThat(response.message).isEqualTo(USER_DOES_NOT_EXIST)
    }

    @Test
    fun `login is invoked but password is incorrect, return Resource Error with correct message`() = runTest {
        // given
        coEvery { userDataSource.getUserByEmail(email) } returns user

        // when
        val response = authRepo.login(email, "12344") // password incorrect
        println(response)

        // then
        assertThat(response).isInstanceOf(Response.Error::class.java)
        assertThat(response.message).isEqualTo(PASSWORD_MISMATCH)
    }

    @Test
    fun `login is invoked and is logged in, return Resource Success with correct message`() = runTest {
        // given
        coEvery { userDataSource.getUserByEmail(email) } returns user

        // when
        val response = authRepo.login(email, password)
        println(response)

        // then
        assertThat(response).isInstanceOf(Response.Success::class.java)
        assertThat(response.message).isEqualTo(SUCCESS_LOGIN)
    }

    @Test
    fun `register is invoked but user already exists, return Resource Error with correct message`() = runTest {
        // given
        coEvery { userDataSource.doesUserExistByEmail(email) } returns true
        coEvery { userDataSource.getUserByEmail(email) } returns user

        // when
        val response = authRepo.registerUser(username, email, password)
        println(response)

        // then
        assertThat(response).isInstanceOf(Response.Error::class.java)
        assertThat(response.message).isEqualTo(USER_ALREADY_EXISTS)
    }

    @Test
    fun `register is invoked but username exists, return Resource Error with correct message`() = runTest {
        // given
        coEvery { userDataSource.doesUserExistByEmail(email) } returns false
        coEvery { userDataSource.doesUserExistByUsername(username) } returns true
        coEvery { userDataSource.getUserByEmail(email) } returns user

        // when
        val response = authRepo.registerUser(username, email, password)
        println(response)

        // then
        assertThat(response).isInstanceOf(Response.Error::class.java)
        assertThat(response.message).isEqualTo(USERNAME_TAKEN)
    }

    @Test
    fun `register is invoked and successfully registers, return Resource Success with correct message`() = runTest {
        // given
        coEvery { userDataSource.doesUserExistByEmail(email) } returns false
        coEvery { userDataSource.doesUserExistByUsername(username) } returns false
        coEvery { userDataSource.getUserByEmail(email) } returns user
        coJustRun { userDataSource.insertUser(any()) }

        // when
        val response = authRepo.registerUser(user.userName, user.email, user.password) // password incorrect
        println(response)

        // then
        assertThat(response).isInstanceOf(Response.Success::class.java)
        assertThat(response.message).isEqualTo(SUCCESS_REGISTER)
    }
}
