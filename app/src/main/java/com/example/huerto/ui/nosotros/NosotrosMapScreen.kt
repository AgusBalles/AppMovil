package com.example.huerto.ui.nosotros


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*
import kotlinx.coroutines.delay

@Composable
fun NosotrosMapScreen() {
    // Tiendas
    val stores = remember {
        listOf(
            "Santiago" to LatLng(-33.4489, -70.6693),
            "Viña del Mar" to LatLng(-33.0245, -71.5518),
            "Valparaíso" to LatLng(-33.0472, -71.6127),
            "Concepción" to LatLng(-36.8201, -73.0444),
            "Nacimiento" to LatLng(-37.5039, -72.6756),
            "Villarrica" to LatLng(-39.2857, -72.2279),
            "Puerto Montt" to LatLng(-41.4689, -72.9411),
        )
    }

    // Estado de cámara: vista inicial neutra
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-35.6751, -71.5430), 4.5f)
    }

    // Marcamos cuando el mapa terminó de cargar sus tiles/engine
    var mapLoaded by remember { mutableStateOf(false) }

    // 🔒 Encadre SEGURO: se ejecuta una sola vez cuando el mapa está listo
    LaunchedEffect(mapLoaded) {
        if (mapLoaded && stores.isNotEmpty()) {
            try {
                delay(50)
                val b = LatLngBounds.Builder()
                stores.forEach { (_, p) -> b.include(p) }
                val bounds = b.build()
                val update = CameraUpdateFactory.newLatLngBounds(bounds,120)
                cameraPositionState.move(update)
            } catch (t: Throwable) {
                t.printStackTrace()

            }
        }
    }

    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = true,
                compassEnabled = true,
                myLocationButtonEnabled = false
            ),
            properties = MapProperties(mapType = MapType.NORMAL),
            onMapLoaded = { mapLoaded = true }
        ) {
            // Marcadores
            stores.forEach { (name, latLng) ->
                Marker(
                    state = MarkerState(position = latLng),
                    title = name,
                    snippet = "Tienda Huerto en $name"
                )
            }
        }
    }
}