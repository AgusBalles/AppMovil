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
import com.huerto.app.ui.login.LoginScreen

object Routes {
    const val LOGIN = "login"
    const val HOME = "home"
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
                    nav.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.HOME) {
            HomeScreen(
                onLoggedOut = {
                    // Si haces logout, te llevo a login
                    nav.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }
    }

    // Reacciona si cambia el estado de login desde DataStore
    LaunchedEffect(logged) {
        if (logged) {
            nav.navigate(Routes.HOME) { popUpTo(0) }
        } else {
            nav.navigate(Routes.LOGIN) { popUpTo(0) }
        }
    }
}