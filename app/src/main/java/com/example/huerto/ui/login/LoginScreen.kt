package com.example.huerto.ui.login

import android.app.Application
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.huerto.data.local.UserPrefs
import com.example.huerto.data.repository.SessionRepository
import com.example.huerto.ui.login.LoginViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val app = LocalContext.current.applicationContext as Application
    val vm: LoginViewModel = viewModel(factory = LoginVMFactory(app))

    val ui by vm.ui.collectAsState()
    val ctx = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val vibrator = ctx.getSystemService(Vibrator::class.java)

    fun buzz() {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            vibrator?.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        else @Suppress("DEPRECATION") vibrator?.vibrate(50)
    }

    Surface(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Card(Modifier.fillMaxWidth(0.9f)) {
                Column(
                    Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("🌿 Bienvenido a Huerto", style = MaterialTheme.typography.headlineSmall)
                    Text("Inicia sesión para continuar", style = MaterialTheme.typography.bodyMedium)

                    OutlinedTextField(
                        value = ui.email,
                        onValueChange = { vm.onEmailChange(it) },
                        label = { Text("Correo electrónico") },
                        leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) },
                        isError = ui.emailError != null,
                        supportingText = { ui.emailError?.let { Text(it) } },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = ui.password,
                        onValueChange = { vm.onPasswordChange(it) },
                        label = { Text("Contraseña") },
                        leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null) },
                        visualTransformation = PasswordVisualTransformation(),
                        isError = ui.passwordError != null,
                        supportingText = { ui.passwordError?.let { Text(it) } },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    AnimatedContent(
                        targetState = ui.isLoading,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "loginLoading"
                    ) { loading ->
                        Button(
                            onClick = {
                                vm.login {
                                    buzz()
                                    Toast.makeText(ctx, "Inicio correcto", Toast.LENGTH_SHORT).show()
                                    onLoginSuccess()
                                }
                            },
                            enabled = ui.canSubmit && !loading,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (loading) {
                                CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
                                Spacer(Modifier.width(8.dp))
                                Text("Verificando…")
                            } else {
                                Text("Iniciar sesión")
                            }
                        }
                    }

                    ui.loginError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }

                    // 👇 ESTE es el botón que ves. Ahora sí navega.
                    TextButton(onClick = { onNavigateToRegister() }) {
                        Text("¿No tienes cuenta? Regístrate")
                    }
                }
            }
        }
    }
}

class LoginVMFactory(private val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repo = SessionRepository(UserPrefs(app))
        return LoginViewModel(repo) as T
    }
}
