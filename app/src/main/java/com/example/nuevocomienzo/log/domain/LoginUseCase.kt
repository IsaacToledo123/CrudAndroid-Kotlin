package com.example.nuevocomienzo.log.domain

import com.example.nuevocomienzo.log.data.model.LoginDTO
import com.example.nuevocomienzo.log.data.repository.LoginRepository

class LoginUseCase {
    private val repository = LoginRepository()
    private val validateLogin = ValidateLoginUseCase()

    suspend operator fun invoke(username: String, password: String): Result<LoginDTO> {
        // First validate the input
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