package com.example.huerto.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huerto.data.repository.SessionRepository
import com.example.huerto.domain.validation.Validators
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val canSubmit: Boolean = false,
    val loginError: String? = null
)

class LoginViewModel(private val repo: SessionRepository) : ViewModel() {

    private val _ui = MutableStateFlow(LoginUiState())
    val ui: StateFlow<LoginUiState> = _ui

    fun onEmailChange(v: String) = update { it.copy(email = v) }.validate()
    fun onPasswordChange(v: String) = update { it.copy(password = v) }.validate()

    private fun update(block: (LoginUiState) -> LoginUiState): LoginViewModel {
        _ui.value = block(_ui.value)
        return this
    }

    private fun validate(): LoginViewModel {
        val s = _ui.value
        val e = Validators.emailErrorOrNull(s.email)
        val p = Validators.passwordErrorOrNull(s.password)
        _ui.value = s.copy(emailError = e, passwordError = p, canSubmit = e == null && p == null)
        return this
    }

    fun login(onSuccess: () -> Unit) = viewModelScope.launch {
        val s = _ui.value
        validate()
        if (!_ui.value.canSubmit) return@launch
        _ui.value = _ui.value.copy(isLoading = true, loginError = null)
        delay(800)
        if (s.email.lowercase().startsWith("huerto") && s.password == "123456") {
            repo.login(s.email, "Usuario Huerto")
            _ui.value = _ui.value.copy(isLoading = false)
            onSuccess()
        } else {
            _ui.value = _ui.value.copy(isLoading = false, loginError = "Credenciales incorrectas")
        }
    }
}