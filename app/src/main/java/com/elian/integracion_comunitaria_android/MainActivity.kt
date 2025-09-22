package com.elian.integracion_comunitaria_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.elian.integracion_comunitaria_android.data.UserSessionManager
import com.elian.integracion_comunitaria_android.data.remote.api.ApiClient
import com.elian.integracion_comunitaria_android.ui.screens.*
import com.elian.integracion_comunitaria_android.ui.theme.Integracion_comunitaria_androidTheme
import com.elian.integracion_comunitaria_android.ui.viewmodel.AuthViewModel
import com.elian.integracion_comunitaria_android.ui.viewmodel.AuthViewModelFactory
import com.elian.integracion_comunitaria_android.ui.viewmodel.NotificationHistoryViewModel
import com.elian.integracion_comunitaria_android.ui.viewmodel.NotificationViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sessionManager = UserSessionManager(this)
        ApiClient.init(sessionManager)

        setContent {
            Integracion_comunitaria_androidTheme {
                val authViewModelFactory = remember { AuthViewModelFactory(sessionManager) }
                AppNavigation(authViewModelFactory = authViewModelFactory)
            }
        }
    }
}

@Composable
fun AppNavigation(authViewModelFactory: AuthViewModelFactory) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)
    // Instanciamos los ViewModels para notificaciones

    val notificationViewModel: NotificationViewModel = viewModel()
    val notificationHistoryViewModel: NotificationHistoryViewModel = viewModel()

    val isAuthenticated = authViewModel.isAuthenticated.value

    NavHost(
        navController = navController,
        startDestination = if (isAuthenticated) "dashboard" else "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate("register") },
                authViewModel = authViewModel
            )
        }
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                authViewModel = authViewModel
            )
        }
        composable("dashboard") {
            DashboardScreen(
                onNavigateToNotifications = { navController.navigate("notifications") },
                // Asegúrate de añadir el onLogout aquí como te indiqué anteriormente
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("login") {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                },
                authViewModel = authViewModel
            )
        }
        composable("notifications") {
            NotificationsScreen(
                navController = navController,
                viewModel = notificationViewModel
            )
        }
        composable(
            route = "history/{notificationId}",
            arguments = listOf(navArgument("notificationId") { type = NavType.IntType })
        ) { backStackEntry ->
            val notificationId = backStackEntry.arguments?.getInt("notificationId") ?: 0
            NotificationHistoryScreen(
                notificationId = notificationId,
                navController = navController,
                viewModel = notificationHistoryViewModel
            )
        }
        composable("create_notification") {
            CreateNotificationScreen(
                navController = navController,
                viewModel = notificationViewModel
            )
        }
    }
}

// ▼▼▼ ¡ASEGÚRATE DE BORRAR LA CLASE AuthViewModel DE AQUÍ! ▼▼▼
// class AuthViewModel(...) { ... } // <- ¡ESTO ES LO QUE HAY QUE ELIMINAR!