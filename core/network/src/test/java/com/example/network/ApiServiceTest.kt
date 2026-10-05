package com.example.network

import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ApiService

    @Before
    fun setUp() {
        // Ek asli (lekin fake/local) HTTP server start hota hai
        mockWebServer = MockWebServer()
        mockWebServer.start()

        // Real Retrofit banate hain, lekin baseUrl mock server ki dete hain
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getUsers parses JSON response into UserDto list correctly`() = runTest {
        // Arrange — server ko batao agli request pe ye JSON body do
        val jsonResponse = """
            [
              {
                "id": 1,
                "name": "John Doe",
                "username": "johnd",
                "email": "john@test.com",
                "address": { "city": "Mumbai" }
              }
            ]
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
        )

        // Act — asli Retrofit call, asli HTTP request localhost pe jaati hai
        val result = apiService.getUser()

        // Assert — Gson ne JSON ko sahi se UserDto mein parse kiya ya nahi
        assertEquals(1, result.size)
        assertEquals("John Doe", result[0].name)
        assertEquals("Mumbai", result[0].address.city)
    }

    @Test
    fun `getPostsByUser sends userId as query parameter in URL`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("[]")
        )

    //    apiService.getPostsByUser(userId = 5)

        // Verify karo — actual request jo bhej gayi thi, uska URL check karo
        val recordedRequest = mockWebServer.takeRequest()
        assertEquals("/posts?userId=5", recordedRequest.path)
    }

    @Test
    fun `getUsers throws HttpException when server returns 404`() = runTest {
        mockWebServer.enqueue(MockResponse().setResponseCode(404))

        try {
            apiService.getUser()
            assert(false) { "Expected HttpException to be thrown" }
        } catch (e: retrofit2.HttpException) {
            assertEquals(404, e.code())
        }
    }

    @Test
    fun `getUsers throws HttpException when server returns 500`() = runTest {
        mockWebServer.enqueue(MockResponse().setResponseCode(500))

        try {
            apiService.getUser()
            assert(false) { "Expected HttpException to be thrown" }
        } catch (e: retrofit2.HttpException) {
            assertEquals(500, e.code())
        }
    }
}
