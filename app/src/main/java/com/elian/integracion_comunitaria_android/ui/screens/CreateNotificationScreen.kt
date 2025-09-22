package com.elian.integracion_comunitaria_android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.elian.integracion_comunitaria_android.ui.theme.AccentColor
import com.elian.integracion_comunitaria_android.ui.theme.BorderColor
import com.elian.integracion_comunitaria_android.ui.theme.ButtonColor
import com.elian.integracion_comunitaria_android.ui.theme.CardBackground
import com.elian.integracion_comunitaria_android.ui.theme.DarkBlueGradientEnd
import com.elian.integracion_comunitaria_android.ui.theme.DarkBlueGradientStart
import com.elian.integracion_comunitaria_android.ui.theme.MutedColor
import com.elian.integracion_comunitaria_android.ui.theme.TextColor
import com.elian.integracion_comunitaria_android.ui.viewmodel.NotificationViewModel

@Composable
fun CreateNotificationScreen(navController: NavController, viewModel: NotificationViewModel) {
    var message by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listOf(DarkBlueGradientStart, DarkBlueGradientEnd))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(500.dp) // Ancho intermedio para el formulario
                .clip(RoundedCornerShape(12.dp))
                .background(CardBackground)
                .padding(horizontal = 30.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Crear Nueva Notificación",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = AccentColor,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Mensaje de la notificación", color = MutedColor) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp), // Mayor altura para un mensaje más largo
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextColor,
                    unfocusedTextColor = TextColor,
                    focusedContainerColor = CardBackground.copy(alpha = 0.5f),
                    unfocusedContainerColor = CardBackground.copy(alpha = 0.5f),
                    focusedBorderColor = AccentColor,
                    unfocusedBorderColor = BorderColor,
                    cursorColor = AccentColor
                ),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (message.isNotBlank()) { // Solo crear si el mensaje no está vacío
                        viewModel.createNotification(message)
                        navController.popBackStack() // Volver a la pantalla anterior
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
                shape = RoundedCornerShape(8.dp),
                enabled = message.isNotBlank() // Habilitar solo si hay mensaje
            ) {
                Text("Crear Notificación", color = Color.White, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.popBackStack() }, // Simplemente vuelve atrás
                modifier = Modifier
                    .fillMaxWidth(0.6f) // Un poco más pequeño
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MutedColor.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Cancelar", color = TextColor, fontSize = 16.sp)
            }
        }
    }
}
