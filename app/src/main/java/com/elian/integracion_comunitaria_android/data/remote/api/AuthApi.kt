package com.elian.integracion_comunitaria_android.data.remote.api

import com.elian.integracion_comunitaria_android.data.models.LoginRequest
import com.elian.integracion_comunitaria_android.data.models.LoginResponse
import com.elian.integracion_comunitaria_android.data.models.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    // CORRECCIÓN AQUÍ
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    // CORRECCIÓN AQUÍ
    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<Unit>
}