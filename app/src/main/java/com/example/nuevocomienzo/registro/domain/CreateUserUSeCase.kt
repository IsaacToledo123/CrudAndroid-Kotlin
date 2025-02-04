package com.example.nuevocomienzo.registro.domain

import com.example.nuevocomienzo.registro.data.model.CreateUserRequest
import com.example.nuevocomienzo.registro.data.model.UserDTO
import com.example.nuevocomienzo.registro.data.repository.RegisterRepository

class CreateUserUSeCase {
    private  val repository = RegisterRepository()

    suspend operator fun invoke(user: CreateUserRequest) : Result<UserDTO> {
        val result = repository.createUser(user)

        //En caso de existir acá debe estar la lógica de negocio
        return result
    }
}