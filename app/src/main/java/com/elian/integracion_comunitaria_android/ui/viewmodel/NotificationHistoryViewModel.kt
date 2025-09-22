package com.elian.integracion_comunitaria_android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.integracion_comunitaria_android.data.models.NotificationHistoryDTO
import com.elian.integracion_comunitaria_android.data.remote.api.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotificationHistoryViewModel : ViewModel() {
    private val _history = MutableStateFlow<List<NotificationHistoryDTO>>(emptyList())
    val history = _history.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // ▼▼▼ AÑADIDO: StateFlow para comunicar errores a la UI ▼▼▼
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private var currentPage = 0
    private var isLastPage = false
    private var currentNotificationId: Int = 0

    fun loadHistoryFor(notificationId: Int) {
        // Si ya se está cargando el historial para este ID, no hacer nada.
        if (currentNotificationId == notificationId && _history.value.isNotEmpty()) return

        // Resetea el estado para la nueva carga
        currentNotificationId = notificationId
        currentPage = 0
        isLastPage = false
        _history.value = emptyList()
        loadMoreHistory()
    }

     fun loadMoreHistory() {
        if (isLoading.value || isLastPage) return

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                // ▼▼▼ CORREGIDO: Manejo robusto de la respuesta de la API ▼▼▼
                val response = ApiClient.notificationService.getNotificationHistory(
                    notificationId = currentNotificationId, // Parámetro corregido
                    page = currentPage,
                    size = 10
                )

                if (response.isSuccessful) {
                    val page = response.body()
                    if (page != null) {
                        _history.value += page.content
                        isLastPage = page.last
                        if (!isLastPage) {
                            currentPage++
                        }
                    } else {
                        _errorMessage.value = "La respuesta del servidor está vacía."
                    }
                } else {
                    _errorMessage.value = "Error al cargar el historial: ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}