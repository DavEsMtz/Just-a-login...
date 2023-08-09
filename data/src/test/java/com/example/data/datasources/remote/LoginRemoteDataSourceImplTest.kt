package com.example.data.datasources.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.data.datasources.remote.apiservices.LoginApiService
import com.example.data.executor.LoginRemoteDataSource
import com.example.data.models.LoginUserResponse
import com.example.domain.DataSourceResultState
import com.example.domain.models.OccupationInfo
import com.example.domain.models.UserInfo
import com.example.domain.repositories.LoginErrorType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import retrofit2.Response

class LoginRemoteDataSourceImplTest {

    @get:Rule
    val testInstantTaskExecutor = InstantTaskExecutorRule()

    @Mock
    private lateinit var apiService: LoginApiService

    private val remoteDataSource: LoginRemoteDataSource by lazy {
        LoginRemoteDataSourceImpl(apiService)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `Test success api response`() = suspendedTest {
        // given
        whenever(apiService.loginUser("user", "pass"))
            .thenReturn(Response.success(LoginUserResponse("200", testUserInfo)))

        val expected = DataSourceResultState.Success(testUserInfo)

        // when
        val result = remoteDataSource.postLoginUser("user", "pass")

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `Test general failed api response`() = suspendedTest {
        // given
        whenever(apiService.loginUser("user", "pass"))
            .thenReturn(Response.error(404, "Stranger Things happened".toResponseBody()))

        val expected = DataSourceResultState.Error(LoginErrorType.GeneralFailure)

        // when
        val result = remoteDataSource.postLoginUser("user", "pass")

        // then
        assertEquals(expected, result)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}

private fun suspendedTest(body: suspend CoroutineScope.() -> Unit) {
    runBlocking { body() }
}

val testUserInfo = UserInfo(
    userId = "1974",
    userName = "Leather Face",
    country = "EEUA",
    occupationInfo = OccupationInfo(
        name = "Serial Killer",
        place = "Texas", holidaysPeriod = null
    )
)