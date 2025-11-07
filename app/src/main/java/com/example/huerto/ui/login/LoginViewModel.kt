package com.example.huerto.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.huerto.data.local.UserPrefs
import com.example.huerto.data.repository.SessionRepository
import com.example.huerto.data.repository.UserRepository
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

class LoginViewModel(app: Application) : AndroidViewModel(app) {
    private val userRepo = UserRepository(app)
    private val sessionRepo = SessionRepository(UserPrefs(app))

    private val _ui = MutableStateFlow(LoginUiState())
    val ui: StateFlow<LoginUiState> = _ui

    fun onEmailChange(v: String) { _ui.value = _ui.value.copy(email = v); validate() }
    fun onPasswordChange(v: String) { _ui.value = _ui.value.copy(password = v); validate() }

    private fun validate() {
        val s = _ui.value
        val e = Validators.emailErrorOrNull(s.email)
        val p = Validators.passwordErrorOrNull(s.password)
        _ui.value = s.copy(emailError = e, passwordError = p, canSubmit = e == null && p == null)
    }

    fun login(onSuccess: () -> Unit) = viewModelScope.launch {
        validate()
        if (!_ui.value.canSubmit) return@launch
        _ui.value = _ui.value.copy(isLoading = true, loginError = null)
        delay(300)
        val s = _ui.value
        val user = userRepo.login(s.email.trim(), s.password)
        if (user != null) {
            sessionRepo.login(user.email, user.name)
            _ui.value = _ui.value.copy(isLoading = false)
            onSuccess()
        } else {
            _ui.value = _ui.value.copy(isLoading = false, loginError = "Credenciales incorrectas")
        }
    }
}