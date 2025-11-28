package com.example.huerto.ui.nosotros

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.*
import com.google.android.gms.maps.model.*

@Composable
fun NosotrosMapScreen() {

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

    // ⭐ CAMBIAMOS EL ZOOM INICIAL (bug fix para Maps Compose)
    val initialPosition = LatLng(-35.0, -71.0)
    val cameraState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialPosition, 7.2f)
    }

    Column(Modifier.fillMaxSize()) {
        Text(
            "Nuestras Tiendas",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraState
        ) {
            stores.forEach { (name, coords) ->
                Marker(
                    state = MarkerState(coords),
                    title = name,
                    snippet = "Sucursal de Huerto"
                )
            }
        }
    }
}
