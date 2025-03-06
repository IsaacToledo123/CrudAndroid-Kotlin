package com.example.nuevocomienzo.registro.data.model


data class CreateUserRequest(
    val username: String,
    val nombre: String,
    val apellidos: String,
    val email: String,
    val password: String,
    val tipoUser: String,
    )