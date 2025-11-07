package com.example.huerto.ui.product


import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.huerto.data.local.db.entities.ProductEntity
import com.example.huerto.data.repository.ProductRepository
import com.example.huerto.ui.home.CartViewModel
import kotlinx.coroutines.launch

@Composable
fun ProductosTab(
    onGoCart: () -> Unit = {} // callback para navegar al carrito
) {
    val app = LocalContext.current.applicationContext as Application
    val repo = remember { ProductRepository(app) }

    LaunchedEffect(Unit) { repo.seedIfEmpty() }

    val productos by repo.observeAll().collectAsState(initial = emptyList())

    // Carrito
    val cartVm: CartViewModel = viewModel(factory = CartViewModel.provideFactory(app))
    val cartUi by cartVm.ui.collectAsState()
    val qtyByProduct = remember(cartUi.items) { cartUi.items.associate { it.productId to it.quantity } }

    // Dialogo para agregar productos
    var showAdd by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            if (cartUi.count > 0) {
                ExtendedFloatingActionButton(
                    onClick = onGoCart,
                    icon = { Icon(Icons.Filled.ShoppingCart, null) },
                    text = { Text("Ir al carrito (${cartUi.count})  ·  $${cartUi.total}") }
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("🛒 Productos", style = MaterialTheme.typography.titleLarge)

            // Botón central para agregar nuevos productos
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Button(onClick = { showAdd = true }) { Text("Agregar productos") }
            }

            if (productos.isEmpty()) {
                Text("Cargando productos…")
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 96.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(productos, key = { it.id }) { p ->
                        ElevatedCard(Modifier.fillMaxWidth()) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ProductImagePlaceholder()
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(p.name, style = MaterialTheme.typography.titleMedium)
                                    Text("$${p.price}", style = MaterialTheme.typography.bodyMedium)
                                    Text(
                                        "Stock: ${p.stock}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                val qty = qtyByProduct[p.id] ?: 0
                                QuantityControl(
                                    quantity = qty,
                                    onMinus = { if (qty > 0) cartVm.dec(p.id, qty) },
                                    onPlus = { if (qty < p.stock) cartVm.add(p.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Diálogo para crear producto
    if (showAdd) {
        AlertDialog(
            onDismissRequest = { showAdd = false },
            title = { Text("Agregar producto") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
                    OutlinedTextField(value = price, onValueChange = { price = it.filter { c -> c.isDigit() } }, label = { Text("Precio") })
                    OutlinedTextField(value = stock, onValueChange = { stock = it.filter { c -> c.isDigit() } }, label = { Text("Stock") })
                }
            },
            confirmButton = {
                val enabled = name.isNotBlank() && price.isNotBlank() && stock.isNotBlank()
                TextButton(
                    enabled = enabled,
                    onClick = {
                        scope.launch {
                            repo.insertAll(
                                ProductEntity(
                                    name = name.trim(),
                                    price = price.toInt(),
                                    stock = stock.toInt()
                                )
                            )
                        }
                        name = ""; price = ""; stock = ""
                        showAdd = false
                    }
                ) { Text("Guardar") }
            },
            dismissButton = { TextButton(onClick = { showAdd = false }) { Text("Cancelar") } }
        )
    }
}

/* --------- Helpers --------- */

@Composable
private fun QuantityControl(
    quantity: Int,
    onMinus: () -> Unit,
    onPlus: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedButton(onClick = onMinus, enabled = quantity > 0) { Text("−") }
        Spacer(Modifier.width(10.dp))
        Text("$quantity", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.width(10.dp))
        Button(onClick = onPlus) { Text("+") }
    }
}

@Composable
private fun ProductImagePlaceholder(size: Dp = 64.dp) {
    Card(modifier = Modifier.size(size), shape = MaterialTheme.shapes.medium) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Image, contentDescription = "imagen", modifier = Modifier.size(size * 0.6f))
        }
    }
}