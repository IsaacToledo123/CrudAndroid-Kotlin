package com.example.nuevocomienzo.registro.domain

import com.example.nuevocomienzo.registro.data.model.UsernameValidateDTO
import com.example.nuevocomienzo.registro.data.repository.RegisterRepository

class UsernameValidateUseCase {
    private  val repository = RegisterRepository()

    suspend operator fun invoke() : Result<UsernameValidateDTO> {
        val result  = repository.validateUsername()

        return result
    }
}