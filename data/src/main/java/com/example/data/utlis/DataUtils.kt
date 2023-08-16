package com.example.data.utlis

import android.content.Intent
import android.os.Build.VERSION_CODES
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import com.example.domain.repositories.LoginErrorType

/**
 *  Method to handle error codes and return an specific case
 *  for this PoC
 *
 */
fun handleErrorCode(errorCode: String): LoginErrorType {
    return when (errorCode) {
        "1010" -> LoginErrorType.SuspendedUser
        else ->
            LoginErrorType.SomeCodeError(errorCode)
    }
}

inline fun <reified T : Parcelable> Intent.retrieveParcelable(key: String): T? = when {
    SDK_INT >= VERSION_CODES.TIRAMISU -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    SDK_INT >= VERSION_CODES.TIRAMISU -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

