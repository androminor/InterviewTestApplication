package com.example.network


import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * REAL integration test — asli internet pe jaata hai, JSONPlaceholder ke
 * real server ko hit karta hai. Ye:
 * - Slow hai (network latency)
 * - Flaky ho sakta hai (agar JSONPlaceholder down ho ya internet na ho)
 * - CI/CD pipeline mein normally SKIP kiya jaata hai
 *
 * Isliye @Ignore laga rakha hai by default — manually run karna ho to
 * @Ignore line comment out kar do ya IDE mein right-click > Run karo,
 * JUnit @Ignore hote hue bhi IDE se force-run ho sakta hai.
 *
 * Purpose: confirm karna ki poora real-world setup (BaseURL, timeout,
 * Gson config) actual JSONPlaceholder API ke saath kaam karta hai —
 * MockWebServer sirf hamare assumptions test karta hai, real API ka
 * actual current response shape nahi.
 */
class ApiServiceRealIntegrationTest {

    private lateinit var apiService: ApiService

    @Before
    fun setUp() {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()

        apiService = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Test
    fun `real API returns user should return non Empty list`() = runTest {
        val users = apiService.getUser()

        assertTrue(users.isNotEmpty())

        assertTrue(users.all { it.username.isNotEmpty() })
        assertTrue(users.all { it.address.city.isNotEmpty() })
    }

    @Test
    fun `real API users should return list`() = runTest {
        val users = apiService.getUser()
        assertTrue(users.all { it.id > 0 })
        assertTrue(users.all { it.email.contains("@") })
    }


}
