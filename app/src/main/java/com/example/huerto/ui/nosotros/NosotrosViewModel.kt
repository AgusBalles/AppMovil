package com.example.huerto.ui.nosotros

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.huerto.data.api.GoogleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class NosotrosUi(
    val lat: Double? = null,
    val lng: Double? = null,
    val loading: Boolean = false,
    val error: String? = null
)

class NosotrosViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = GoogleRepository()

    private val _ui = MutableStateFlow(NosotrosUi())
    val ui = _ui.asStateFlow()

    fun load(address: String, key: String) {
        viewModelScope.launch {
            _ui.value = NosotrosUi(loading = true)

            val coords = repo.getCoords(address, key)
            _ui.value = if (coords != null) {
                NosotrosUi(lat = coords.first, lng = coords.second)
            } else {
                NosotrosUi(error = "No se encontró la dirección")
            }
        }
    }
}
