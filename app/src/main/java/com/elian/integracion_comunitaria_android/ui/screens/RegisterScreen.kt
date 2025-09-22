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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// import androidx.lifecycle.viewmodel.compose.viewModel // Ya no es necesario
import com.elian.integracion_comunitaria_android.ui.theme.*
import com.elian.integracion_comunitaria_android.ui.viewmodel.AuthViewModel
// import com.elian.integracion_comunitaria_android.ui.viewmodel.AuthViewModelFactory // Ya no se necesita

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    authViewModel: AuthViewModel // <<<--- CAMBIO: Recibe el ViewModel
) {
    // val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory) // <<<--- Se elimina esta línea

    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isLoading = authViewModel.isLoading.value
    val errorMessage = authViewModel.errorMessage.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listOf(DarkBlueGradientStart, DarkBlueGradientEnd))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(400.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(CardBackground)
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Registro de Usuario",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = AccentColor,
                modifier = Modifier.padding(bottom = 25.dp),
                textAlign = TextAlign.Center
            )

            // Usando el mismo estilo de TextField que en LoginScreen para consistencia
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre", color = MutedColor) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextColor,
                    unfocusedTextColor = TextColor,
                    focusedBorderColor = AccentColor,
                    unfocusedBorderColor = BorderColor
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(15.dp))
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Apellido", color = MutedColor) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextColor,
                    unfocusedTextColor = TextColor,
                    focusedBorderColor = AccentColor,
                    unfocusedBorderColor = BorderColor
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(15.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = MutedColor) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextColor,
                    unfocusedTextColor = TextColor,
                    focusedBorderColor = AccentColor,
                    unfocusedBorderColor = BorderColor
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(15.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña", color = MutedColor) },
                visualTransformation = PasswordVisualTransformation(),
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

            Button(
                onClick = {
                    // Se pasa el callback onRegisterSuccess directamente
                    authViewModel.register(name, lastName, email, password, onRegisterSuccess)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Registrarse", color = Color.White)
                }
            }

            errorMessage?.let {
                Text(
                    text = it,
                    color = ErrorColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 10.dp).fillMaxWidth()
                )
            }
        }
    }
}