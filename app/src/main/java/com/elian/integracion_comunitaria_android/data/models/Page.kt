package com.elian.integracion_comunitaria_android.data.models

/**
 * Representa una página de resultados devuelta por la API.
 * Es una clase genérica, por lo que puede contener cualquier tipo de dato en 'content'.
 * @param T El tipo de los objetos en la lista de contenido (ej. NotificationDTO, NotificationHistoryDTO).
 */
data class Page<T>(
    val content: List<T>,
    val totalPages: Int,
    val totalElements: Long,
    val last: Boolean,
    val first: Boolean,
    val number: Int, // El número de la página actual (empezando en 0)
    val size: Int,
    val empty: Boolean
)