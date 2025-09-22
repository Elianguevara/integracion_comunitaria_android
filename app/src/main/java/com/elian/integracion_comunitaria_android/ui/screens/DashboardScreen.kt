package com.elian.integracion_comunitaria_android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elian.integracion_comunitaria_android.ui.theme.*
import com.elian.integracion_comunitaria_android.ui.viewmodel.AuthViewModel

@Composable
fun DashboardScreen(
    onNavigateToNotifications: () -> Unit,
    onLogout: () -> Unit, // Parámetro para el logout
    authViewModel: AuthViewModel
) {
    val userEmail = authViewModel.getUserEmail()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listOf(DarkBlueGradientStart, DarkBlueGradientEnd))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(600.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(CardBackground)
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Bienvenido al Dashboard",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = AccentColor,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            if (userEmail != null) {
                Text(
                    text = "Sesión iniciada como: $userEmail",
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
            }

            Button(
                onClick = onNavigateToNotifications,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonColor)
            ) {
                Text("Ver Notificaciones", color = Color.White, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onLogout, // Llama a la función de logout
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Cerrar Sesión", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}