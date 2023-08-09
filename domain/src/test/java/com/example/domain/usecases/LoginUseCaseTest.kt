package com.example.domain.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.Event
import app.cash.turbine.test
import com.example.domain.DataSourceResultState
import com.example.domain.models.OccupationInfo
import com.example.domain.models.UserInfo
import com.example.domain.repositories.LoginErrorType
import com.example.domain.repositories.LoginRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.anyString
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class LoginUseCaseTest {

    @get:Rule
    val testInstantTaskExecutor = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: LoginRepository

    private val loginUseCase: LoginUseCase by lazy {
        LoginUseCase(repository)
    }

    @Before
    @OptIn(ExperimentalCoroutinesApi::class)
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `Test loginUseCase success result`() = suspendedTest {
        // given
        whenever(repository.loginUser(anyString(), anyString())).thenReturn(
            flowOf(
                DataSourceResultState.Success(testUserInfo)
            )
        )

        val expected = DataSourceResultState.Success(testUserInfo)

        // when
        loginUseCase.invoke(anyString(), anyString()).test {

            val result = cancelAndConsumeRemainingEvents()

            // then
            with(result) {
                assertFalse(isEmpty())
                assertTrue(contains(Event.Item(expected)))
            }
        }
    }

    @Test
    fun `Test loginUseCase general failure request`() = suspendedTest {
        // given
        whenever(repository.loginUser(anyString(), anyString())).thenReturn(
            flowOf(DataSourceResultState.Error(LoginErrorType.GeneralFailure))
        )

        val expected = DataSourceResultState.Error(LoginErrorType.GeneralFailure)

        // when
        loginUseCase.invoke(anyString(), anyString()).test {

            val result = cancelAndConsumeRemainingEvents()

            // then
            with(result) {
                assertFalse(isEmpty())
                assertTrue(contains(Event.Item(expected)))
            }
        }
    }
}

private fun suspendedTest(body: suspend CoroutineScope.() -> Unit) {
    runBlocking { body() }
}

/* Data Testing */
val testUserInfo = UserInfo(
    userId = "1974",
    userName = "Leather Face",
    country = "EEUA",
    occupationInfo = OccupationInfo(
        name = "Serial Killer",
        place = "Texas", holidaysPeriod = null
    )
)