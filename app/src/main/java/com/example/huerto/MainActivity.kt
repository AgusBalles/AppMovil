package com.example.huerto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.example.huerto.data.local.UserPrefs
import com.example.huerto.data.repository.SessionRepository
import com.example.huerto.ui.navigation.AppNav
import com.example.huerto.ui.theme.HuertoTheme // Si no tienes HuertoTheme, usa MaterialTheme directamente

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val session = SessionRepository(UserPrefs(applicationContext))

        setContent {
            // Si no creaste HuertoTheme aún, reemplaza por: MaterialTheme { ... }
            HuertoTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val nav = rememberNavController()
                    AppNav(nav = nav, session = session)
                }
            }
        }
    }
}