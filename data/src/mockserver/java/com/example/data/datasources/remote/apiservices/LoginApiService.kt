package com.example.data.datasources.remote.apiservices

import com.example.data.models.LoginUserResponse
import com.mustafayigit.mockresponseinterceptor.Mock
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginApiService {

    @POST("with-a-too-insecure-login/")
    @Mock
    suspend fun loginUser(
        @Query("userName") userName: String,
        @Query("userPass") userPass: String
    ): Response<LoginUserResponse>
}