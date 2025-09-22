package com.elian.integracion_comunitaria_android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// import androidx.lifecycle.viewmodel.compose.viewModel // Ya no es necesario
import com.elian.integracion_comunitaria_android.ui.theme.*
import com.elian.integracion_comunitaria_android.ui.viewmodel.AuthViewModel
// import com.elian.integracion_comunitaria_android.ui.viewmodel.AuthViewModelFactory // Ya no se necesita

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    authViewModel: AuthViewModel // <<<--- CAMBIO 1: Recibe el ViewModel, no la factory.
) {
    // val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory) // <<<--- CAMBIO 2: Se elimina esta línea.

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val errorMessage = authViewModel.errorMessage.value
    val isLoading = authViewModel.isLoading.value // Opcional: para mostrar un indicador de carga

    // Efecto secundario para navegar solo cuando el estado de autenticación cambia a `true`.
    LaunchedEffect(authViewModel.isAuthenticated.value) {
        if (authViewModel.isAuthenticated.value) {
            onLoginSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listOf(DarkBlueGradientStart, DarkBlueGradientEnd))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(360.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(CardBackground)
                .padding(40.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Integración Comunitaria",
                    style = MaterialTheme.typography.titleLarge,
                    color = AccentColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Unidos crecemos",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextColor,
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp),
                    textAlign = TextAlign.Center
                )
            }
            Text(
                text = "Inicio de Sesión",
                style = MaterialTheme.typography.titleLarge,
                color = TextColor,
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .fillMaxWidth(),
                fontSize = 28.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Ingresa tu email", color = MutedColor) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextColor,
                    unfocusedTextColor = TextColor,
                    focusedBorderColor = AccentColor,
                    unfocusedBorderColor = BorderColor
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(25.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Ingresa tu contraseña", color = MutedColor) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextColor,
                    unfocusedTextColor = TextColor,
                    focusedBorderColor = AccentColor,
                    unfocusedBorderColor = BorderColor
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = { authViewModel.login(email, password) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
                shape = RoundedCornerShape(6.dp),
                enabled = !isLoading // Deshabilitar el botón mientras carga
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Enviar", color = Color.White, modifier = Modifier.padding(vertical = 4.dp))
                }
            }
            errorMessage?.let {
                Text(
                    text = it,
                    color = ErrorColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .fillMaxWidth()
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("¿No tenes cuenta? ", textAlign = TextAlign.Center, color = TextColor, style = MaterialTheme.typography.bodyMedium)
                TextButton(onClick = onRegisterClick) {
                    Text("Regístrate aquí", textAlign = TextAlign.Center, color = AccentColor, textDecoration = TextDecoration.Underline)
                }
            }
        }
    }
}