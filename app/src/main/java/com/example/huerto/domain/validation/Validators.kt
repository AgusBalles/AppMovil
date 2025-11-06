package com.example.huerto.domain.validation

object Validators {
    fun emailErrorOrNull(value: String): String? {
        val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return when {
            value.isBlank() -> "El correo es obligatorio"
            !regex.matches(value) -> "Correo inválido"
            else -> null
        }
    }

    fun passwordErrorOrNull(value: String): String? =
        when {
            value.isBlank() -> "La contraseña es obligatoria"
            value.length < 6 -> "Debe tener al menos 6 caracteres"
            else -> null
        }
}