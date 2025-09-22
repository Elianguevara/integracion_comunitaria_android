package com.elian.integracion_comunitaria_android.data.remote.api
// En tu archivo NotificationApi.kt
import com.elian.integracion_comunitaria_android.data.models.*
import retrofit2.Response // <--- Importante añadir esto
import retrofit2.http.*

interface NotificationApi {

    // ▼▼▼ EJEMPLO DE CAMBIO ▼▼▼
    // Antes: fun getNotifications(...): Page<NotificationDTO>
    // Después:
    @GET("api/notifications")
    suspend fun getNotifications(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<Page<NotificationDTO>> // <-- Envuelto en Response

    // Aplica el mismo cambio a todos los demás endpoints
    @POST("api/notifications")
    suspend fun createNotification(@Body request: NotificationCreateRequest): Response<NotificationDTO>

    @PATCH("api/notifications/{id}/view")
    suspend fun markAsViewed(@Path("id") notificationId: Int): Response<NotificationDTO>

    @DELETE("api/notifications/{id}")
    suspend fun deleteNotification(@Path("id") notificationId: Int): Response<Unit>

    @GET("api/notifications/{id}/history")
    suspend fun getNotificationHistory(
        @Path("id") notificationId: Int,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<Page<NotificationHistoryDTO>>
}