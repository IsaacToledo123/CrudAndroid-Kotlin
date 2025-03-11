package com.example.nuevocomienzo.login.domain

import com.example.nuevocomienzo.login.data.model.LoginDTO
import com.example.nuevocomienzo.login.data.repository.LoginRepository

class LoginUseCase {
    private val repository = LoginRepository()
    private val validateLogin = ValidateLoginUseCase()

    suspend operator fun invoke(username: String, password: String): Result<LoginDTO> {
        val validationResult = validateLogin(username, password)

        return validationResult.fold(
            onSuccess = { loginRequest ->
                repository.login(loginRequest.email, loginRequest.password)
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }
}