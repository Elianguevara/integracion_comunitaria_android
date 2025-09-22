package com.elian.integracion_comunitaria_android.data.remote.api

import com.elian.integracion_comunitaria_android.data.UserSessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val sessionManager: UserSessionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        // ▼▼▼ ESTA ES LA LÍNEA CORREGIDA ▼▼▼
        sessionManager.getAuthToken()?.let { token ->
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}