// ruta: integracion_comunitaria_android/app/src/main/java/com/elian/integracion_comunitaria_android/MainActivity.kt

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
import com.elian.integracion_comunitaria_android.ui.viewmodel.NotificationViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sessionManager = UserSessionManager(this)
        ApiClient.init(sessionManager)

        setContent {
            Integracion_comunitaria_androidTheme {
                // La factory del AuthViewModel se crea una sola vez
                val authViewModelFactory = remember { AuthViewModelFactory(sessionManager) }
                AppNavigation(authViewModelFactory = authViewModelFactory)
            }
        }
    }
}

@Composable
fun AppNavigation(authViewModelFactory: AuthViewModelFactory) {
    val navController = rememberNavController()
    // El AuthViewModel se crea aquí porque se comparte entre login, register y dashboard
    val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)
    val isAuthenticated = authViewModel.isAuthenticated.value

    NavHost(
        navController = navController,
        startDestination = if (isAuthenticated) "dashboard" else "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("dashboard") { popUpTo("login") { inclusive = true } }
                },
                onRegisterClick = { navController.navigate("register") },
                authViewModel = authViewModel
            )
        }
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("login") { popUpTo("register") { inclusive = true } }
                },
                authViewModel = authViewModel
            )
        }
        composable("dashboard") {
            DashboardScreen(
                onNavigateToNotifications = { navController.navigate("notifications") },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("login") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                },
                authViewModel = authViewModel
            )
        }
        composable("notifications") {
            // El NotificationViewModel se crea aquí, solo cuando se necesita
            val notificationViewModel: NotificationViewModel = viewModel()
            NotificationsScreen(
                navController = navController,
                viewModel = notificationViewModel
            )
        }
        composable("create_notification") {
            // Reutilizamos el ViewModel de la pantalla anterior si es necesario, o creamos uno nuevo
            val notificationViewModel: NotificationViewModel = viewModel()
            CreateNotificationScreen(
                navController = navController,
                viewModel = notificationViewModel
            )


        }
        composable(
            // Ruta más descriptiva
            route = "notification_history/{notificationId}",
            arguments = listOf(navArgument("notificationId") { type = NavType.IntType })
        ) { backStackEntry ->
            val notificationId = backStackEntry.arguments?.getInt("notificationId")

            if (notificationId != null) {
                // El NotificationHistoryViewModel y el NavController no se pasan como parámetro
                // La pantalla los gestiona internamente
                NotificationHistoryScreen(notificationId = notificationId,
                    navController = navController)
            }
        }
    }
}