package com.example.huerto.ui.home


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.huerto.data.local.UserPrefs
import com.example.huerto.data.local.db.dao.CartLine
import com.example.huerto.data.repository.CartRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class CartUiState(
    val items: List<CartLine> = emptyList(),
    val count: Int = 0,
    val total: Int = 0,
    val email: String = ""
)

class CartViewModel(
    private val app: Application
) : ViewModel() {

    private val prefs = UserPrefs(app)
    private val repo = CartRepository(app)

    private val _ui = MutableStateFlow(CartUiState())
    val ui: StateFlow<CartUiState> = _ui.asStateFlow()

    init {
        viewModelScope.launch {
            // Reacciona a cambios de sesión (email)
            prefs.email.flatMapLatest { email ->
                if (email.isBlank()) {
                    flowOf(Triple(emptyList<CartLine>(), 0, 0) to email)
                } else {
                    combine(
                        repo.observeCart(email),
                        repo.observeCount(email),
                        repo.observeTotal(email)
                    ) { list, count, total ->
                        Triple(list, count, total) to email
                    }
                }
            }.collect { (triple, email) ->
                val (list, count, total) = triple
                _ui.value = CartUiState(items = list, count = count, total = total, email = email)
            }
        }
    }

    private inline fun withEmail(block: (String) -> Unit) {
        val email = _ui.value.email
        if (email.isNotBlank()) block(email)
    }

    fun add(productId: Long) = withEmail { email ->
        viewModelScope.launch { repo.add(email, productId, 1) }
    }

    fun inc(productId: Long, current: Int) = withEmail { email ->
        viewModelScope.launch { repo.setQty(email, productId, current + 1) }
    }

    fun dec(productId: Long, current: Int) = withEmail { email ->
        viewModelScope.launch { repo.setQty(email, productId, current - 1) }
    }

    fun remove(productId: Long) = withEmail { email ->
        viewModelScope.launch { repo.remove(email, productId) }
    }

    fun clear() = withEmail { email ->
        viewModelScope.launch { repo.clearForUser(email) }
    }

    companion object {
        /** Factory seguro y simple para Compose */
        fun provideFactory(app: Application): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return CartViewModel(app) as T
                }
            }
    }
}