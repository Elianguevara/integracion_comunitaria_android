// ruta: integracion_comunitaria_android/app/src/main/java/com/elian/integracion_comunitaria_android/ui/viewmodel/NotificationHistoryViewModel.kt

package com.elian.integracion_comunitaria_android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.integracion_comunitaria_android.data.models.NotificationHistoryDTO
import com.elian.integracion_comunitaria_android.data.remote.api.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class NotificationHistoryViewModel : ViewModel() {
    private val _history = MutableStateFlow<List<NotificationHistoryDTO>>(emptyList())
    val history = _history.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private var currentPage = 0
    private var isLastPage = false
    private var currentNotificationId: Int? = null

    fun loadHistoryFor(notificationId: Int) {
        if (currentNotificationId == notificationId && _history.value.isNotEmpty() || isLoading.value) {
            return
        }

        currentNotificationId = notificationId
        currentPage = 0
        isLastPage = false
        _history.value = emptyList()
        loadMoreHistory()
    }

    fun loadMoreHistory() {
        val id = currentNotificationId ?: return
        if (isLoading.value || isLastPage) return

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                // ▼▼▼ CORRECCIÓN PRINCIPAL AQUÍ ▼▼▼
                val response = ApiClient.notificationApi.getNotificationHistory(
                    notificationId = id,
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
                    _errorMessage.value = "Error al cargar el historial (Código: ${response.code()})"
                }
            } catch (e: IOException) {
                _errorMessage.value = "Error de conexión. Revisa tu acceso a internet."
            } catch (e: HttpException) {
                _errorMessage.value = "Error en el servidor (Código: ${e.code()})"
            } catch (e: Exception) {
                _errorMessage.value = "Ocurrió un error inesperado."
            } finally {
                _isLoading.value = false
            }
        }
    }
}