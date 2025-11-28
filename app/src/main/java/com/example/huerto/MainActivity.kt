package com.example.huerto

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.huerto.data.local.UserPrefs
import com.example.huerto.data.repository.SessionRepository
import com.example.huerto.ui.navigation.AppNav
import com.example.huerto.ui.theme.HuertoTheme
import kotlinx.coroutines.launch

// ⭐ IMPORT CORRECTO ⭐
import com.example.huerto.BuildConfig

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("API_KEY_DEBUG", "API Key usada: ${BuildConfig.MAPS_API_KEY}")

        val session = SessionRepository(UserPrefs(applicationContext))

        lifecycleScope.launch { session.logout() }

        setContent {
            HuertoTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val nav = rememberNavController()
                    AppNav(nav = nav, session = session)
                }
            }
        }
    }
}
