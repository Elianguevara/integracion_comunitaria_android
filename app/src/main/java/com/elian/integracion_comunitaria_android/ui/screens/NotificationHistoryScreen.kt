package com.elian.integracion_comunitaria_android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elian.integracion_comunitaria_android.data.models.NotificationHistoryDTO
import com.elian.integracion_comunitaria_android.ui.viewmodel.NotificationHistoryViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull

@Composable
fun NotificationHistoryScreen(
    notificationId: Int,
    navController: NavController,
    viewModel: NotificationHistoryViewModel
) {
    val history by viewModel.history.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val listState = rememberLazyListState()

    // ▼▼▼ CORREGIDO: Llama a la nueva función para cargar el historial ▼▼▼
    LaunchedEffect(notificationId) {
        viewModel.loadHistoryFor(notificationId)
    }

    Scaffold(
        // Puedes agregar un TopAppBar si quieres
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading && history.isEmpty()) {
                // Muestra un indicador de carga inicial
                CircularProgressIndicator()
            } else if (errorMessage != null) {
                // Muestra un mensaje de error si algo falló
                Text(
                    text = errorMessage ?: "Ocurrió un error desconocido",
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            } else if (history.isEmpty()) {
                // Muestra un mensaje si no hay historial
                Text(text = "No hay historial para esta notificación.")
            } else {
                // Muestra la lista de historial
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(history) { historyItem ->
                        HistoryItem(historyItem) // Componente para mostrar cada item
                    }

                    // Muestra el indicador de carga al final de la lista si está cargando más
                    if (isLoading) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                            }
                        }
                    }
                }
            }

            // ▼▼▼ MEJORADO: Lógica de paginación más robusta ▼▼▼
            LaunchedEffect(listState) {
                snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                    .filterNotNull()
                    .distinctUntilChanged()
                    .collect { lastVisibleIndex ->
                        // Si el último item visible es el penúltimo de la lista, carga más.
                        if (lastVisibleIndex >= history.size - 2) {
                            viewModel.loadMoreHistory()
                        }
                    }
            }
        }
    }
}

// ▼▼▼ AÑADIDO: Un Composable de ejemplo para mostrar el item del historial ▼▼▼
// Puedes personalizarlo como quieras.
@Composable
fun HistoryItem(history: NotificationHistoryDTO) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // ▼▼▼ CORREGIDO: Cambia 'eventType' por 'event' ▼▼▼
            Text(text = "Evento: ${history.event}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Fecha: ${history.eventDate}", style = MaterialTheme.typography.bodySmall)
        }
    }
}