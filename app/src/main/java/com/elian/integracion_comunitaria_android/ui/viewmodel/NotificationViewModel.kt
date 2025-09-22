package com.elian.integracion_comunitaria_android.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.integracion_comunitaria_android.data.models.NotificationCreateRequest
import com.elian.integracion_comunitaria_android.data.models.NotificationDTO
import com.elian.integracion_comunitaria_android.data.remote.api.ApiClient
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotificationViewModel : ViewModel() {

    private val _notifications = MutableStateFlow<List<NotificationDTO>>(emptyList())
    val notifications = _notifications.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // ▼▼▼ AÑADIDO: StateFlow para comunicar errores a la UI ▼▼▼
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private var currentPage = 0
    private var _isLastPage = false
    val isLastPage: Boolean
        get() = _isLastPage


    init {
        loadMoreNotifications()
    }

    fun loadMoreNotifications() {
        // Evita cargas múltiples si ya está cargando o si llegó a la última página
        if (_isLoading.value || isLastPage) return

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null // Limpia errores anteriores
            try {
                Log.d("NotificationViewModel", "Attempting to load notifications. Current page: $currentPage")
                val response = ApiClient.notificationService.getNotifications(currentPage,10)
                Log.d("NotificationViewModel", "Response received. Code: ${response.code()}")

                if (response.isSuccessful) {
                    val page = response.body()
                    if (page != null) {
                        Log.d("NotificationViewModel", "Response successful. Page content size: ${page.content.size}, isLast: ${page.last}")
                        _notifications.value += page.content
                        _isLastPage = page.last
                        if (!isLastPage) {
                            currentPage++
                        }
                    } else {
                        Log.e("NotificationViewModel", "Response successful but body is null.")
                        _errorMessage.value = "La respuesta del servidor está vacía."
                    }
                } else {
                    val errorBody = response.errorBody()?.string() // Leer el cuerpo del error solo una vez
                    Log.e("NotificationViewModel", "Error loading notifications. Code: ${response.code()}, Message: ${response.message()}, ErrorBody: $errorBody")
                    _errorMessage.value = "Error al cargar notificaciones: ${response.code()} ${response.message()}"
                }
            } catch (e: Exception) {
                Log.e("NotificationViewModel", "Exception during notification loading", e)
                _errorMessage.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createNotification(message: String) {
        viewModelScope.launch {
            _errorMessage.value = null
            try {
                val request = NotificationCreateRequest(message)
                val response = ApiClient.notificationService.createNotification(request)
                if (response.isSuccessful) {
                    // Refresca la lista para mostrar la nueva notificación al principio
                    refresh() // CORREGIDO: Llamar a refresh() en lugar de refreshNotifications()
                } else {
                    Log.e("NotificationViewModel", "Error creating notification. Code: ${response.code()}, Message: ${response.message()}")
                    _errorMessage.value = "Error al crear la notificación: ${response.message()}"
                }
            } catch (e: Exception) {
                Log.e("NotificationViewModel", "Exception during notification creation", e)
                _errorMessage.value = "Error de conexión: ${e.message}"
            }
        }
    }

    fun markAsViewed(notificationId: Int) {
        viewModelScope.launch {
            try {
                val response = ApiClient.notificationService.markAsViewed(notificationId)
                if (response.isSuccessful) {
                    // Actualiza el estado local sin recargar toda la lista
                    val updatedList = _notifications.value.map {
                        if (it.id == notificationId) it.copy(viewed = true) else it
                    }
                    _notifications.value = updatedList
                } else {
                    Log.e("NotificationViewModel", "Error marking notification as viewed. Code: ${response.code()}, Message: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("NotificationViewModel", "Exception marking notification as viewed", e)
                // Manejar el error si es necesario, por ejemplo, mostrando un mensaje
            }
        }
    }

    fun deleteNotification(notificationId: Int) {
        viewModelScope.launch {
            try {
                val response = ApiClient.notificationService.deleteNotification(notificationId)
                if (response.isSuccessful) {
                    // Elimina el elemento de la lista localmente
                    _notifications.value = _notifications.value.filterNot { it.id == notificationId }
                } else {
                    Log.e("NotificationViewModel", "Error deleting notification. Code: ${response.code()}, Message: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("NotificationViewModel", "Exception deleting notification", e)
                // Manejar el error
            }
        }
    }

    // ▼▼▼ MEJORADO: Ahora se llama 'refresh' y es más claro su propósito ▼▼▼
    fun refresh() {
        currentPage = 0
        _isLastPage = false
        _notifications.value = emptyList()
        loadMoreNotifications() // Inicia la carga desde la primera página
    }
}
