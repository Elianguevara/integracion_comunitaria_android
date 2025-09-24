// ruta: integracion_comunitaria_android/app/src/main/java/com/elian/integracion_comunitaria_android/ui/screens/NotificationHistoryScreen.kt

package com.elian.integracion_comunitaria_android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.elian.integracion_comunitaria_android.data.models.NotificationHistoryDTO
import com.elian.integracion_comunitaria_android.ui.theme.*
import com.elian.integracion_comunitaria_android.ui.viewmodel.NotificationHistoryViewModel

@Composable
fun NotificationHistoryScreen(
    notificationId: Int,
    navController: NavController, // Añadimos NavController para el botón de volver
    viewModel: NotificationHistoryViewModel = viewModel()
) {
    LaunchedEffect(key1 = notificationId) {
        viewModel.loadHistoryFor(notificationId)
    }

    val history by viewModel.history.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

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
                .padding(horizontal = 30.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Historial (ID: $notificationId)",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = AccentColor,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            if (isLoading && history.isEmpty()) {
                CircularProgressIndicator(color = AccentColor, modifier = Modifier.padding(16.dp))
            } else if (errorMessage != null) {
                Text(
                    text = "Error: $errorMessage",
                    color = ErrorColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            } else if (history.isEmpty()) {
                Text(
                    text = "No hay historial para esta notificación.",
                    color = TextColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f)
                ) {
                    items(history) { historyItem ->
                        HistoryItem(historyItem)
                    }

                    if (isLoading && history.isNotEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = AccentColor)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentColor.copy(alpha = 0.8f)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Volver a Notificaciones", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun HistoryItem(history: NotificationHistoryDTO) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground.copy(alpha = 0.7f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(
                text = "Evento: ${history.event}",
                style = MaterialTheme.typography.bodyLarge,
                color = TextColor,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Fecha: ${history.eventDate}",
                style = MaterialTheme.typography.bodySmall,
                color = TextColor.copy(alpha = 0.7f)
            )
        }
    }
}