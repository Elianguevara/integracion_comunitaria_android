package com.elian.integracion_comunitaria_android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.navigation.NavController
import com.elian.integracion_comunitaria_android.data.models.NotificationDTO
import com.elian.integracion_comunitaria_android.ui.theme.AccentColor
import com.elian.integracion_comunitaria_android.ui.theme.ButtonColor
import com.elian.integracion_comunitaria_android.ui.theme.CardBackground
import com.elian.integracion_comunitaria_android.ui.theme.DarkBlueGradientEnd
import com.elian.integracion_comunitaria_android.ui.theme.DarkBlueGradientStart
import com.elian.integracion_comunitaria_android.ui.theme.ErrorColor
import com.elian.integracion_comunitaria_android.ui.theme.TextColor
import com.elian.integracion_comunitaria_android.ui.viewmodel.NotificationViewModel

@Composable
fun NotificationItem(
    notification: NotificationDTO,
    viewModel: NotificationViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.viewed) CardBackground.copy(alpha = 0.4f) else CardBackground.copy(alpha = 0.7f) // Un poco más oscuro o diferente para destacar
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                Text(
                    text = notification.message,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextColor
                )
                Text(
                    text = "ID: ${notification.id}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextColor.copy(alpha = 0.7f)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (!notification.viewed) {
                    IconButton(
                        onClick = { viewModel.markAsViewed(notification.id) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.Done, contentDescription = "Marcar como vista", tint = AccentColor)
                    }
                }
                IconButton(
                    onClick = { viewModel.deleteNotification(notification.id) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar Notificación", tint = ErrorColor)
                }
            }
        }
    }
}

@Composable
fun NotificationsScreen(navController: NavController, viewModel: NotificationViewModel) {
    val notifications by viewModel.notifications.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val listState = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listOf(DarkBlueGradientStart, DarkBlueGradientEnd))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(600.dp) // Ancho similar a DashboardScreen
                .clip(RoundedCornerShape(12.dp))
                .background(CardBackground)
                .padding(horizontal = 30.dp, vertical = 40.dp), // Ajuste de padding
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Notificaciones",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = AccentColor,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Button(
                onClick = { navController.navigate("create_notification") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Crear Nueva Notificación", color = Color.White, fontSize = 18.sp)
            }

            if (isLoading && notifications.isEmpty()) {
                CircularProgressIndicator(color = AccentColor, modifier = Modifier.padding(16.dp))
            }
            else if (errorMessage != null) {
                Text(
                    text = "Error: $errorMessage",
                    color = ErrorColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
            else if (notifications.isEmpty()) {
                Text(
                    text = "No hay notificaciones disponibles.",
                    color = TextColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
            else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxWidth().weight(1f) // Ocupa el espacio restante
                ) {
                    items(notifications, key = { it.id }) { notification ->
                        NotificationItem(
                            notification = notification,
                            viewModel = viewModel
                        )
                    }

                    if (isLoading && notifications.isNotEmpty()) { // Indicador de carga al final de la lista
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
            // Botón para volver al Dashboard (opcional, pero útil)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth(0.6f) // Más pequeño que los otros botones
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentColor.copy(alpha = 0.8f)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Volver al Dashboard", color = Color.White, fontSize = 16.sp)
            }

        }
    }

    // Efecto para cargar más notificaciones al llegar al final
    LaunchedEffect(listState, notifications, isLoading, viewModel.isLastPage) { // CORREGIDO
        val layoutInfo = listState.layoutInfo
        val totalItemsCount = layoutInfo.totalItemsCount
        val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1

        if (!isLoading && !viewModel.isLastPage && notifications.isNotEmpty() && totalItemsCount > 0) {
            if (lastVisibleItemIndex >= notifications.size - 1 - 3 && lastVisibleItemIndex == totalItemsCount -1) {
                viewModel.loadMoreNotifications()
            }
        }
    }
}

// Dummy isLastPage property in ViewModel for LaunchedEffect to compile
// Add this to your actual NotificationViewModel:
// val isLastPage: Boolean
//    get() = _isLastPage // Assuming you have a private _isLastPage mutable state
