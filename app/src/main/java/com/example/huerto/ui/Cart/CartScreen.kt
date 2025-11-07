package com.example.huerto.ui.product

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.huerto.data.local.db.dao.CartLine
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.ArrowBack
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    items: List<CartLine>,
    total: Int,
    onClear: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🛒 Mi Carrito") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    TextButton(onClick = onClear) {
                        Text("Vaciar", color = MaterialTheme.colorScheme.error)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (items.isEmpty()) {
                Text("Tu carrito está vacío 🪴", style = MaterialTheme.typography.bodyLarge)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(items) { item ->
                        Card(Modifier.fillMaxWidth()) {
                            Row(
                                Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(item.name, style = MaterialTheme.typography.bodyLarge)
                                    Text("x${item.quantity} — $${item.price} c/u")
                                }
                                Text("$${item.subtotal}")
                            }
                        }
                    }
                }

                Divider(Modifier.padding(vertical = 8.dp))

                Text("Total: $$total", style = MaterialTheme.typography.titleMedium)
                Button(
                    onClick = { /* Acción de pagar */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Finalizar compra")
                }
            }
        }
    }
}