package com.example.userlist // Corrected package name

import app.cash.turbine.test
import com.example.network.ErrorState
import com.example.network.ResponseState
import com.example.network.model.User
import com.example.user.usecase.GetUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class UserListViewModelTest {
/*

    1. emits initial then loading then the success state
    2. emits error message when the usecase returns NoInternetException
    3. emits error message when the usecase returns ServerDownError
    4. emits error message when the usecase return  NotFoundError
    5. emits loadUser called again resets error and show loading

private val testDispatcher = StandardDispatcher()
private lateinit var usecase:GetUserUseCase
private lateinit var viewmodel:UserListViewModel

private val sampleData = listOf<User> (
  User(id = 1, name = "Ram", username = "P.Ram", email = "ram@wip.com", city = "Bengaluru"),
        User(
            id = 2,
            name = "Manohar",
            username = "Manohar Srinivas",
            email = "sri@wip.com",
            city = "Delhi"
        )
        )
        @Before
        fun setUp() {
        Dispatchers.setMain(testDispatcher)
        usecase = mock()
        }

        @After
        fun tearDown() {
        Dispatchers.resetMain()
        }

        @Test
        fun `emit initial then loading later success state `() = runtTest{
        whenever(usecase()).thenReturn(Response.success(sampleData)
        viewmodel = UserListViewmodel(usecase,testDispatcher)

        viewmodel.uiState.test ->{
        val initialState = awaitItem()
        assertEquals(false,initialState.isLoading)


        val loadingState = awaitItem()
        assertEquals(true,loadingState.isLoading)
        assertEquals(emptyList<User>,loadingState.data)

        val successState = awaitItem()
        assertEquals(false, successState.isLoading)
        assertEquals(sampleUser,successState.data)
        assertEquals(null, successState.errorMessage)
        }
        }

* */

    private val testDispatcher =
        StandardTestDispatcher() // Standard dispatcher is an instance of a dispatcher whose whole tasks are run inside calls to the scheduler.
    private lateinit var getUserUseCase: GetUserUseCase
    private lateinit var viewmodel: UserListViewmodel

    private val sampleUser = listOf<User>(
        User(id = 1, name = "Ram", username = "P.Ram", email = "ram@wip.com", city = "Bengaluru"),
        User(
            id = 2,
            name = "Manohar",
            username = "Manohar Srinivas",
            email = "sri@wip.com",
            city = "Delhi"
        )
    )

    @Before //scheduler executing task 1
    fun setup() {
        Dispatchers.setMain(testDispatcher)//Viewmodel scope android ke main thread se juda hua hai, Ye line Dispatchers.Main ko temporarily replace kar deti hai apne testDispatcher (jo StandardTestDispatcher() because we can not use Dispatcher.Main here as it will crash
        getUserUseCase = mock() //fake dummy object bana raha hai
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // scheduler cleanup task 1
    }

    /*1. emits initial loading then the success state
    2. emits error message when the usecase returns NoInternetException
    3. emits error message when the usecase returns ServerDownError
    4. emits error message when the usecase return  NotFoundError
    5. emits loadUser called again resets error and show loading
    * */

    @Test
    fun `emits initials loading then the success rate `() {
        runTest {
            whenever(getUserUseCase()).thenReturn(ResponseState.Success(sampleUser))
            //passing test dispatcher as the second argument required by the constructor
            viewmodel = UserListViewmodel(getUserUseCase, testDispatcher)
            viewmodel.uiState.test {
                //initial loading
                val initialState = awaitItem() // next event received was an item and return it
                assertEquals(false, initialState.isLoading)

                //Loading state
                val loadingState = awaitItem()
                assertEquals(true, loadingState.isLoading)
                assertEquals(emptyList<User>(), loadingState.data)

                //Success state
                val successState = awaitItem()
                assertEquals(false, successState.isLoading)
                assertEquals(sampleUser, successState.data)
                assertEquals(null, successState.errorMessage)


            }
        }
    }

    @Test
    fun `emits error message when the usecase returns NoInternetException`() = runTest {
        whenever(getUserUseCase()).thenReturn(ResponseState.Error<List<User>>(ErrorState.NoInternetException))
        viewmodel = UserListViewmodel(getUserUseCase, testDispatcher)

        viewmodel.uiState.test {
            awaitItem() // initial state
            awaitItem() // loading state

            val errorState = awaitItem()
            assertEquals(false, errorState.isLoading)
            assertEquals(
                "No internet connection",
                errorState.errorMessage
            )
        }
    }
    @Test
    fun`emits error message when the usecase return  NotFoundError`() = runTest {
        whenever(getUserUseCase()).thenReturn(ResponseState.Error(ErrorState.NotFoundError))
        viewmodel = UserListViewmodel(getUserUseCase,testDispatcher)
        viewmodel.uiState.test {
            awaitItem()
            awaitItem()
            val errorState = awaitItem()
            assertEquals(false,errorState.isLoading)
            assertEquals("User not found",errorState.errorMessage)
        }
    }

    @Test
    fun `emits loadUser called again resets error and show loading`() = runTest {
        whenever(getUserUseCase()).thenReturn(ResponseState.Error<List<User>>(ErrorState.ServerDownError))
        viewmodel = UserListViewmodel(getUserUseCase, testDispatcher)

        //Why we are using advanceUntilIdle() because we are going to stub second time (whenever) first for error then success to simulate retry mechanism
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(
            "Server is currently down please try again",
            viewmodel.uiState.value.errorMessage
        )
        whenever(getUserUseCase()).thenReturn(ResponseState.Success(sampleUser))
        viewmodel.uiState.test {
            viewmodel.loadUsers()
            // When loadUsers() is called, it launches a coroutine.
            // Since we are collecting a StateFlow, the current value is emitted first.
            val currentState = awaitItem()
            assertEquals("Server is currently down please try again", currentState.errorMessage)

            val loadingState = awaitItem()
            assertEquals(true, loadingState.isLoading)

            val successRate = awaitItem()
            assertEquals(false, successRate.isLoading)
            assertEquals(sampleUser, successRate.data)
            assertEquals(null,successRate.errorMessage)
        }
    }
}


