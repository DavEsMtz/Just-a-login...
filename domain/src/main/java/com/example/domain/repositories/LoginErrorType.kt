package com.example.domain.repositories

/* Here we can have as many errors we need to clarify events that occurs or come from login request,
 for this PoC will be only one with code and a generic*/
sealed class LoginErrorType : Throwable() {
    object GeneralFailure : LoginErrorType()
    class SomeCodeError(val code: String) : LoginErrorType()
    object SuspendedUser : LoginErrorType()
}