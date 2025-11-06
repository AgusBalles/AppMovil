package com.example.huerto.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.huerto.data.repository.SessionRepository
import com.example.huerto.ui.home.HomeScreen
import com.example.huerto.ui.register.RegisterScreen
import com.example.huerto.ui.login.LoginScreen

object Routes {
    const val LOGIN = "login"
    const val HOME = "home"
    const val REGISTER = "register"
}

@Composable
fun AppNav(nav: NavHostController, session: SessionRepository) {
    // Observa si hay sesión activa
    val logged by session.isLogged.collectAsState(initial = false)

    // Define la gráfica de navegación
    NavHost(
        navController = nav,
        startDestination = if (logged) Routes.HOME else Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    nav.navigate(Routes.HOME) { popUpTo(Routes.LOGIN) { inclusive = true } }
                },
                onNavigateToRegister = {                       // 👈 nuevo callback
                    nav.navigate(Routes.REGISTER)
                }
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen (
                onRegistered = { email ->
                    // Ejemplo: tras registro volver al login con el email prellenado (opcional)
                    nav.popBackStack()
                },
                onBack = { nav.popBackStack() }
            )
        }
        composable(Routes.HOME) {
            HomeScreen {
                nav.navigate(Routes.LOGIN) { popUpTo(Routes.HOME) { inclusive = true } }
            }
        }
    }

    // Reacciona si cambia el estado de login desde DataStore
    // SOLO redirige automáticamente cuando hay login (true)
    LaunchedEffect(logged) {
        if (logged) {
            nav.navigate(Routes.HOME) { popUpTo(0) }
        }
        // ❌ no fuerces nav a LOGIN cuando logged == false
    }

}
