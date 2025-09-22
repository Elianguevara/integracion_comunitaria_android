package com.elian.integracion_comunitaria_android.data.remote.api

import com.elian.integracion_comunitaria_android.data.UserSessionManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://10.0.2.2:8080/" // Para emulador Android

    private lateinit var retrofit: Retrofit
    private lateinit var sessionManager: UserSessionManager

    val authService: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }

    // ▼▼▼ ESTA ES LA LÍNEA QUE HAY QUE CORREGIR ▼▼▼
    // Antes: val notificationService: NotificationApi by lazy { ... }
    // Después:
    val notificationService: NotificationApi by lazy {
        retrofit.create(NotificationApi::class.java)
    }

    fun init(manager: UserSessionManager) {
        sessionManager = manager

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sessionManager))
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}