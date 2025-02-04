package com.example.nuevocomienzo.log.domain

import com.example.nuevocomienzo.log.data.model.LoginRequest

class ValidateLoginUseCase {
    operator fun invoke(username: String, password: String): Result<LoginRequest> {
        if (username.isBlank()) {
            return Result.failure(Exception("El nombre de usuario no puede estar vacío"))
        }

        if (password.isBlank()) {
            return Result.failure(Exception("La contraseña no puede estar vacía"))
        }

        if (password.length < 6) {
            return Result.failure(Exception("La contraseña debe tener al menos 6 caracteres"))
        }

        // Additional validation rules can be added here
        // For example, checking username format, password complexity, etc.

        return Result.success(LoginRequest(username, password))
    }
}