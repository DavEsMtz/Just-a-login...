package com.example.justalogin.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.Event
import app.cash.turbine.test
import com.example.data.models.parcelable.OccupationInfoParcel
import com.example.data.models.parcelable.UserInfoParcel
import com.example.domain.DataSourceResultState
import com.example.domain.models.OccupationInfo
import com.example.domain.models.UserInfo
import com.example.domain.repositories.LoginErrorType
import com.example.domain.repositories.LoginRepository
import com.example.domain.usecases.LoginUseCase
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
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import kotlin.time.Duration.Companion.seconds

class LoginViewModelTest {

    @get:Rule
    val testInstantTaskExecutor = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: LoginRepository

    private lateinit var loginUseCase: LoginUseCase

    private val viewModel: LoginViewModel by lazy {
        loginUseCase = LoginUseCase(repository)
        LoginViewModel(loginUseCase)
    }

    @Before
    @OptIn(ExperimentalCoroutinesApi::class)
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }


    @Test
    fun `Test invalid user data`() = suspendedTest {
        viewModel.viewState.test {
            // when
            viewModel.validateEntriesForLogin("", "")
            val result = cancelAndConsumeRemainingEvents()

            // then
            assertTrue(result.contains(Event.Item(LoginViewState.EmptyField)))
        }
    }

    @Test
    fun `Test valid user data`() = suspendedTest {
        // given
        whenever(repository.loginUser("name", "pass")).thenReturn(
            flowOf(
                DataSourceResultState.Success(testUserInfo)
            )
        )

        val expectedSuccess = LoginViewState.Success(testUserInfoParcel)
        val expectedEvent = LoginViewEvent.SuccessLogin(testUserInfoParcel)

        viewModel.viewState.test(5.seconds) {
            // when
            viewModel.validateEntriesForLogin("name", "pass")

            // then
            assertEquals(LoginViewState.Idle, awaitItem())
            assertEquals(LoginViewState.Loading, awaitItem())
            assertEquals(expectedSuccess, awaitItem())
        }

        viewModel.viewEvent.observeForever { resultEvent ->
            assertEquals(expectedEvent, resultEvent)
        }
    }


    @Test
    fun `Test login generic failure`() = suspendedTest {
        // given
        whenever(repository.loginUser("name", "pass")).thenReturn(
            flowOf(
                DataSourceResultState.Error(LoginErrorType.GeneralFailure)
            )
        )

        val expectedError = LoginViewState.Failure(LoginErrorType.GeneralFailure)

        viewModel.viewState.test(5.seconds) {
            // when
            viewModel.validateEntriesForLogin("name", "pass")

            // then
            assertEquals(LoginViewState.Idle, awaitItem())
            assertEquals(LoginViewState.Loading, awaitItem())
            assertEquals(expectedError, awaitItem())
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

val testUserInfoParcel = UserInfoParcel(
    userId = "1974",
    userName = "Leather Face",
    country = "EEUA",
    occupationInfo = OccupationInfoParcel(
        name = "Serial Killer",
        place = "Texas", holidaysPeriod = null
    )
)