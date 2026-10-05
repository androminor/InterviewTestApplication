package com.example.user

import com.example.network.ApiService
import com.example.network.ErrorState
import com.example.network.ResponseState
import com.example.network.model.NetworkAddress
import com.example.network.model.NetworkUser
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class UserRepositoryImplTest {

    private lateinit var apiService: ApiService
    private lateinit var repository: UserRepositoryImpl

    @Before
    fun setUp() {
        apiService = mock()
        repository = UserRepositoryImpl(apiService)
    }

    @Test
    fun `getUsers returns Success with correctly mapped domain models`() = runTest {
        // Arrange — DTO structure ke saath, nested address bhi
        val dtos = listOf(
            NetworkUser(
                id = 1,
                name = "John Doe",
                username = "johnd",
                email = "john@test.com",
                address = NetworkAddress(city = "Mumbai")
            ),
            NetworkUser(
                id = 2,
                name = "Jane Smith",
                username = "janes",
                email = "jane@test.com",
                address = NetworkAddress(city = "Delhi")
            )
        )
        whenever(apiService.getUser()).thenReturn(dtos)

        // Act
        val result = repository.getUsers()

        // Assert — Success mila aur mapping sahi hui (especially nested address.city -> flat city)
        assertTrue(result is ResponseState.Success)
        val users = (result as ResponseState.Success).data
        assertEquals(2, users?.size)
        assertEquals("John Doe", users?.get(0)?.name)
        assertEquals("Mumbai", users?.get(0)?.city) // mapping ka sabse important assertion
        assertEquals("Delhi", users?.get(1)?.city)
    }

    @Test
    fun `getUsers returns Success with empty list when api returns empty list`() = runTest {
        whenever(apiService.getUser()).thenReturn(emptyList())

        val result = repository.getUsers()

        assertTrue(result is ResponseState.Success)
        assertEquals(emptyList<Any>(), (result as ResponseState.Success).data)
    }

    @Test
    fun `getUsers returns NotFoundError when api throws 404`() = runTest {
        val httpException = HttpException(
            Response.error<List<NetworkUser>>(404, "".toResponseBody(null))
        )
        whenever(apiService.getUser()).thenAnswer { throw httpException }

        val result = repository.getUsers()

        assertTrue(result is ResponseState.Error)
        assertEquals(ErrorState.NotFoundError, (result as ResponseState.Error).error)
    }

    @Test
    fun `getUsers returns ServerDownError when api throws 500`() = runTest {
        val httpException = HttpException(
            Response.error<List<NetworkUser>>(500, "".toResponseBody(null))
        )
        whenever(apiService.getUser()).thenAnswer { throw httpException }

        val result = repository.getUsers()

        assertTrue(result is ResponseState.Error)
        assertEquals(ErrorState.ServerDownError, (result as ResponseState.Error).error)
    }

    @Test
    fun `getUsers returns NoInternetException when UnknownHostException thrown`() = runTest {
        whenever(apiService.getUser()).thenAnswer { throw UnknownHostException() }

        val result = repository.getUsers()

        assertTrue(result is ResponseState.Error)
        assertEquals(ErrorState.NoInternetException, (result as ResponseState.Error).error)
    }

    @Test
    fun `getUsers returns ServerDownError when SocketTimeoutException thrown`() = runTest {
        whenever(apiService.getUser()).thenAnswer { throw SocketTimeoutException() }

        val result = repository.getUsers()

        assertTrue(result is ResponseState.Error)
        assertEquals(ErrorState.ServerDownError, (result as ResponseState.Error).error)
    }
}