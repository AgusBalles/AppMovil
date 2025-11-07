@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.huerto.ui.register

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RegisterScreen(
    onRegistered: (email: String) -> Unit,
    onBack: () -> Unit
) {
    val vm: RegisterViewModel = viewModel()
    val ui by vm.ui.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Crear cuenta") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver atrás")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Campos de formulario
            OutlinedTextField(
                value = ui.email,
                onValueChange = vm::onEmail,
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = ui.name,
                onValueChange = vm::onName,
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = ui.pass,
                onValueChange = vm::onPass,
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = ui.pass2,
                onValueChange = vm::onPass2,
                label = { Text("Repite contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            ui.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            // Botón principal de registro
            Button(
                onClick = { vm.submit(onRegistered) },
                enabled = ui.canSubmit && !ui.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (ui.isLoading) {
                    CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
                    Spacer(Modifier.width(8.dp))
                    Text("Creando cuenta…")
                } else {
                    Text("Registrarme")
                }
            }

        }
    }
}