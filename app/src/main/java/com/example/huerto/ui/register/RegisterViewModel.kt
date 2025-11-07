package com.example.huerto.ui.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.huerto.data.repository.UserRepository
import com.example.huerto.domain.validation.Validators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class RegisterUiState(
    val email: String = "",
    val name: String = "",
    val pass: String = "",
    val pass2: String = "",
    val error: String? = null,
    val isLoading: Boolean = false,
    val canSubmit: Boolean = false
)

class RegisterViewModel(app: Application) : AndroidViewModel(app) {
    private val userRepo = UserRepository(app)

    private val _ui = MutableStateFlow(RegisterUiState())
    val ui: StateFlow<RegisterUiState> = _ui

    fun onEmail(v: String) { _ui.value = _ui.value.copy(email = v); validate() }
    fun onName(v: String)  { _ui.value = _ui.value.copy(name = v); validate() }
    fun onPass(v: String)  { _ui.value = _ui.value.copy(pass = v); validate() }
    fun onPass2(v: String) { _ui.value = _ui.value.copy(pass2 = v); validate() }

    private fun validate() {
        val s = _ui.value
        val emailErr = Validators.emailErrorOrNull(s.email)
        val passErr  = Validators.passwordErrorOrNull(s.pass)
        val pass2Err = if (s.pass == s.pass2) null else "Las contraseñas no coinciden"
        val anyErr = emailErr ?: passErr ?: pass2Err
        _ui.value = s.copy(error = anyErr, canSubmit = anyErr == null && s.name.isNotBlank())
    }

    fun submit(onSuccess: (String) -> Unit) = viewModelScope.launch {
        validate()
        val s = _ui.value
        if (!s.canSubmit) return@launch
        _ui.value = s.copy(isLoading = true, error = null)
        val res = userRepo.register(s.email.trim(), s.name.trim(), s.pass)
        _ui.value = _ui.value.copy(isLoading = false)
        res.fold(
            onSuccess = { onSuccess(s.email) },
            onFailure = {
                _ui.value = _ui.value.copy(error = "Ese correo ya está registrado o ocurrió un error")
            }
        )
    }
}