package com.example.nuevocomienzo.registro.data.model


data class CreateUserRequest(
    val id:Int = 0,
    val nombre: String,
    val apellidos: String,
    val email: String,
    val password: String,
    val tipo_user: String,
    )