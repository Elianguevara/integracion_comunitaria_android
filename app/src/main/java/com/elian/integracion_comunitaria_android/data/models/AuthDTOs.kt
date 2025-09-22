package com.elian.integracion_comunitaria_android.data.models

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String
)

data class RegisterRequest(
    val name: String,
    val lastName: String,
    val email: String,
    val password: String,
    val roleType: String
)