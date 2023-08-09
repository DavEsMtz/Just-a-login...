package com.example.justalogin.ui.login


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.mappers.toParcel
import com.example.data.models.parcelable.UserInfoParcel
import com.example.domain.DataSourceResultState
import com.example.domain.repositories.LoginErrorType
import com.example.domain.usecases.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {

    private val _viewState = MutableStateFlow<LoginViewState>(LoginViewState.Idle)
    val viewState get() = _viewState

    private val _viewEvent = MutableLiveData<LoginViewEvent?>()
    val viewEvent get() = _viewEvent

    /**
     * Executes from [LoginUseCase] login process and updates viewState
     * @param userName
     * @param userPass
     */
    private fun loginUser(userName: String, userPass: String) {
        viewModelScope.launch {
            _viewState.emit(LoginViewState.Loading)
            /*Just for PoC purposes*/ delay(3000)
            loginUseCase.invoke(userName, userPass).collect { dataState ->
                when (dataState) {
                    is DataSourceResultState.Error -> {
                        _viewState.emit(LoginViewState.Failure(dataState.error))
                    }

                    is DataSourceResultState.Success -> {
                        _viewState.emit(LoginViewState.Success(dataState.data.toParcel()))
                    }
                }
            }
        }
    }

    /**
     * Validates that user entry is correct and valid
     * @param userName
     * @param userPass
     */
    fun validateEntriesForLogin(userName: String, userPass: String) {
        /* Here only validate that are not empty but here
        we could create or map an object to send as body,
        cypher pass or whatever we need before call use case*/
        viewModelScope.launch {
            if (userName.isEmpty() || userPass.isEmpty()) {
                _viewState.emit(LoginViewState.EmptyField)
            } else {
                loginUser(userName, userPass)
            }
        }
    }

    /**
     * Handle and update about a feature is not implemented yet
     */
    fun handleNotImplemented() {
        _viewEvent.value = LoginViewEvent.NotImplemented
    }

    /**
     * Sets state on idle state, waiting user input
     */
    fun bePrepared() {
        viewModelScope.launch {
            _viewState.emit(LoginViewState.Idle)
            _viewEvent.value = null
        }
    }

    /**
     * Handles error type to update event
     * @param error specific [LoginErrorType]
     */
    fun handleError(error: LoginErrorType) {
        /* Here we can handle error and take action depending errorCode
        * sending event if is necessary
        *   E.g.  is LoginErrorType.SuspendedUser -> _viewEvent.value = LoginViewEvent.SuspendedUser

        * for this case we won't update any specific event and only will return
        * to the idle state*/
        when ((error)) {
            /* Just for PoC purposes */
            is LoginErrorType.GeneralFailure,
            is LoginErrorType.SomeCodeError,
            is LoginErrorType.SuspendedUser -> {
                bePrepared()
            }
        }
    }

    /**
     * Handles success login result and update event
     * @param userInfo
     */
    fun loginSuccess(userInfo: UserInfoParcel) {
        _viewEvent.value = LoginViewEvent.SuccessLogin(userInfo)
    }
}

sealed class LoginViewEvent {
    data class SuccessLogin(val userInfo: UserInfoParcel) : LoginViewEvent()
    object TooManyTries : LoginViewEvent()
    object SuspendedUser : LoginViewEvent()
    object NotImplemented : LoginViewEvent()
}

sealed class LoginViewState {
    object Idle : LoginViewState()
    object Loading : LoginViewState()
    data class Success(val userInfo: UserInfoParcel) : LoginViewState()
    data class Failure(val error: Throwable) : LoginViewState()
    object EmptyField : LoginViewState()
}
