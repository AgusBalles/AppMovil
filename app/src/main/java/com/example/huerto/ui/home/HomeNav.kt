package com.example.huerto.ui.home

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.huerto.data.repository.ProductRepository
import kotlinx.coroutines.launch

sealed class HomeRoutes(
    val route: String,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    object Inicio : HomeRoutes("inicio", "Inicio", Icons.Filled.Home)
    object Productos : HomeRoutes("productos", "Productos", Icons.Filled.LocalFireDepartment)
    object Carrito : HomeRoutes("carrito", "Carrito", Icons.Filled.ShoppingCart)
}

@Composable
fun HomeNav(onLogout: () -> Unit) {
    val nav = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(nav) }
    ) { padding ->
        NavHost(
            navController = nav,
            startDestination = HomeRoutes.Inicio.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(HomeRoutes.Inicio.route) {
                InicioScreen(onLogout = onLogout)
            }
            composable(HomeRoutes.Productos.route) {
                ProductosScreen()
            }
            composable(HomeRoutes.Carrito.route) {
                CartScreen()
            }
        }
    }
}

@Composable
fun BottomBar(nav: NavHostController) {
    val app = LocalContext.current.applicationContext as Application
    val cartVm: CartViewModel = viewModel(factory = CartViewModel.provideFactory(app))
    val cart by cartVm.ui.collectAsState()

    val items = listOf(HomeRoutes.Inicio, HomeRoutes.Productos, HomeRoutes.Carrito)
    NavigationBar {
        val navBackStackEntry by nav.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    nav.navigate(item.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(nav.graph.startDestinationId) { saveState = true }
                    }
                },
                icon = {
                    if (item == HomeRoutes.Carrito && cart.count > 0) {
                        BadgedBox(badge = { Badge { Text(cart.count.toString()) } }) {
                            Icon(item.icon, contentDescription = item.title)
                        }
                    } else {
                        Icon(item.icon, contentDescription = item.title)
                    }
                },
                label = { Text(item.title) }
            )
        }
    }
}

/* -----------------------------------------------------
   PANTALLA: INICIO (limpia carrito al cerrar sesión)
----------------------------------------------------- */
@Composable
private fun InicioScreen(onLogout: () -> Unit) {
    val app = LocalContext.current.applicationContext as Application
    val cartVm: CartViewModel = viewModel(factory = CartViewModel.provideFactory(app))
    val scope = rememberCoroutineScope()

    Surface(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("🌿 Bienvenido a tu Huerto Digital", style = MaterialTheme.typography.headlineSmall)
                Text(
                    "Desde aquí puedes acceder a tus productos o cerrar sesión.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = {
                        scope.launch {
                            cartVm.clear() // limpia carrito del usuario logueado
                            onLogout()     // cierra sesión
                        }
                    }) {
                        Text("Cerrar sesión")
                    }
                }
            }
        }
    }
}

/* -----------------------------------------------------
   PANTALLA: PRODUCTOS (agregar al carrito)
----------------------------------------------------- */
@Composable
private fun ProductosScreen() {
    val app = LocalContext.current.applicationContext as Application
    val repo = remember { ProductRepository(app) }
    val productos by repo.observeAll().collectAsState(initial = emptyList())

    // VM del carrito
    val cartVm: CartViewModel = viewModel(factory = CartViewModel.provideFactory(app))
    val cartUi by cartVm.ui.collectAsState()

    // Mapa productId -> cantidad en carrito
    val qtyByProduct = remember(cartUi.items) {
        cartUi.items.associate { it.productId to it.quantity }
    }

    Surface(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("🛒 Catálogo", style = MaterialTheme.typography.titleLarge)

            if (productos.isEmpty()) {
                Text("No hay productos registrados aún.")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(productos, key = { it.id }) { p ->
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Imagen del producto (desde res/drawable)
                                ProductImage(name = p.name)

                                Spacer(Modifier.width(12.dp))

                                Column(Modifier.weight(1f)) {
                                    Text(
                                        p.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text("Precio: $${p.price}", style = MaterialTheme.typography.bodyMedium)
                                    Text("Stock: ${p.stock}", style = MaterialTheme.typography.bodySmall)
                                }

                                // Controles + / −
                                val inCart = qtyByProduct[p.id] ?: 0
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    OutlinedButton(
                                        onClick = { if (inCart > 0) cartVm.dec(p.id, inCart) },
                                        enabled = inCart > 0
                                    ) { Text("−") }

                                    Spacer(Modifier.width(8.dp))
                                    Text("$inCart", style = MaterialTheme.typography.titleMedium)
                                    Spacer(Modifier.width(8.dp))

                                    val canAdd = inCart < p.stock
                                    Button(
                                        onClick = { if (canAdd) cartVm.add(p.id) },
                                        enabled = canAdd
                                    ) { Text("+") }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/* ---------- Imagen local desde res/drawable ---------- */

@Composable
private fun ProductImage(name: String, size: Dp = 72.dp) {
    val resId = productImageRes(name)  // siempre devuelve un drawable válido
    Card(
        modifier = Modifier.size(size),
        shape = MaterialTheme.shapes.medium
    ) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@androidx.annotation.DrawableRes
private fun productImageRes(name: String): Int {
    // Normaliza: minúsculas + sin tildes/acentos
    val raw = name.trim().lowercase()
    val n = java.text.Normalizer.normalize(raw, java.text.Normalizer.Form.NFD)
        .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")

    return when {
        "espinaca" in n   -> com.example.huerto.R.drawable.espinaca
        "platano" in n    -> com.example.huerto.R.drawable.platano
        "zanahoria" in n  -> com.example.huerto.R.drawable.zanahoria
        "pimenton" in n     -> com.example.huerto.R.drawable.pimenton
        "quinua" in n     -> com.example.huerto.R.drawable.quinua
        else              -> com.example.huerto.R.drawable.placeholder
    }
}

/* ---------------------- CARRITO ---------------------- */

@Composable
private fun CartScreen() {
    val app = LocalContext.current.applicationContext as Application
    val vm: CartViewModel = viewModel(factory = CartViewModel.provideFactory(app))
    val ui by vm.ui.collectAsState()

    Surface(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("🧺 Carrito", style = MaterialTheme.typography.titleLarge)

            if (ui.items.isEmpty()) {
                Text("Tu carrito está vacío.")
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(ui.items, key = { it.id }) { line ->
                        ListItem(
                            headlineContent = { Text(line.name) },
                            supportingContent = {
                                Text("Precio $${line.price} · Subtotal $${line.subtotal}")
                            },
                            trailingContent = {
                                Row {
                                    OutlinedButton(onClick = {
                                        vm.dec(line.productId, line.quantity)
                                    }) { Text("-") }
                                    Spacer(Modifier.width(8.dp))
                                    Text("${line.quantity}", style = MaterialTheme.typography.titleMedium)
                                    Spacer(Modifier.width(8.dp))
                                    OutlinedButton(onClick = {
                                        vm.inc(line.productId, line.quantity)
                                    }) { Text("+") }
                                }
                            }
                        )
                        Divider()
                    }
                }

                Text("TOTAL: $${ui.total}", style = MaterialTheme.typography.headlineSmall)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick = { vm.clear() },
                        modifier = Modifier.weight(1f)
                    ) { Text("Vaciar") }
                    Button(
                        onClick = { /* TODO: flujo de pago */ },
                        modifier = Modifier.weight(1f)
                    ) { Text("Pagar") }
                }
            }
        }
    }
}