package com.example.huerto.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.huerto.data.repository.SessionRepository
import com.example.huerto.ui.home.HomeScreen
import com.example.huerto.ui.login.LoginScreen
import com.example.huerto.ui.register.RegisterScreen

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
}

@Composable
fun AppNav(nav: NavHostController, session: SessionRepository) {
    val logged by session.isLogged.collectAsState(initial = false)

    NavHost(
        navController = nav,
        startDestination = if (logged) Routes.HOME else Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    nav.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToRegister = { nav.navigate(Routes.REGISTER) }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegistered = { nav.popBackStack() },
                onBack = { nav.popBackStack() }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                onLoggedOut = {
                    nav.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}