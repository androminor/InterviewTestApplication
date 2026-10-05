package com.example.user


import com.example.network.ErrorState
import com.example.network.ResponseState
import com.example.network.model.User
import com.example.user.repository.UserRepository
import com.example.user.usecase.GetUserUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetUsersUseCaseTest {

    private lateinit var repository: UserRepository
    private lateinit var useCase: GetUserUseCase

    private val sampleUsers = listOf(
        User(id = 1, name = "John", username = "johnd", email = "john@test.com", city = "Mumbai")
    )

    @Before
    fun setUp() {
        repository = mock()
        useCase = GetUserUseCase(repository)
    }

    @Test
    fun `invoke returns Success from repository as-is`() = runTest {
        whenever(repository.getUsers()).thenReturn(ResponseState.Success(sampleUsers))

        val result = useCase()

        assertEquals(ResponseState.Success(sampleUsers), result)
    }

    @Test
    fun `invoke returns Error from repository as-is`() = runTest {
        whenever(repository.getUsers()).thenReturn(
            ResponseState.Error(ErrorState.NoInternetException)
        )

        val result = useCase()

        assertEquals(ResponseState.Error<List<User>>(ErrorState.NoInternetException), result)
    }

    @Test
    fun `invoke delegates exactly once to repository getUsers`() = runTest {
        whenever(repository.getUsers()).thenReturn(ResponseState.Success(sampleUsers))

        useCase()

        // Confirm karte hain repository.getUsers() exactly ek baar call hua
        verify(repository).getUsers()
    }
}