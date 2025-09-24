// Archivo: data/models/NotificationDTOs.kt

package com.elian.integracion_comunitaria_android.data.models

import java.time.LocalDateTime

data class NotificationDTO(
    val id: Int,
    val providerId: Int?,
    val customerId: Int?,
    val message: String,
    val viewed: Boolean,
    val dateCreate: String,
    val deleted: Boolean
)

data class NotificationHistoryDTO(
    val id: Int,
    val notificationId: Int,
    val event: String,
    val eventDate: String,
    val userId: Int?,
    val dateCreate: String
)

data class NotificationCreateRequest(
    val message: String
)