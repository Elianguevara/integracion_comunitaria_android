package com.elian.integracion_comunitaria_android.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.integracion_comunitaria_android.data.UserSessionManager
import com.elian.integracion_comunitaria_android.data.models.LoginRequest
import com.elian.integracion_comunitaria_android.data.models.RegisterRequest
import com.elian.integracion_comunitaria_android.data.remote.api.ApiClient
import kotlinx.coroutines.launch

class AuthViewModel(private val sessionManager: UserSessionManager) : ViewModel() {
    private val _isAuthenticated = mutableStateOf(sessionManager.isUserLoggedIn())
    val isAuthenticated: State<Boolean> = _isAuthenticated

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = ApiClient.authService.login(LoginRequest(email, password))

                if (response.isSuccessful) {
                    val token = response.body()?.token
                    if (token != null) {
                        sessionManager.saveAuthToken(token)
                        _isAuthenticated.value = true
                    } else {
                        _errorMessage.value = "La respuesta del servidor no es válida."
                    }
                } else {
                    _errorMessage.value = "Error: Email o contraseña incorrectos."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun register(name: String, lastName: String, email: String, password: String, onRegisterSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = ApiClient.authService.register(RegisterRequest(name, lastName, email, password, "CUSTOMER"))
                if (response.isSuccessful) {
                    onRegisterSuccess()
                } else {
                    _errorMessage.value = "Error en el registro. Código: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error en el registro: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ▼▼▼ FUNCIÓN AÑADIDA PARA CERRAR SESIÓN ▼▼▼
    fun logout() {
        // 1. Limpia el token y los datos de la sesión guardada.
        sessionManager.clearSession()
        // 2. Actualiza el estado para notificar a la UI que el usuario ya no está autenticado.
        _isAuthenticated.value = false
    }

    fun getUserEmail(): String? {
        return sessionManager.getUserEmail()
    }

}