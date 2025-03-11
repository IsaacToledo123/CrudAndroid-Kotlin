package com.example.nuevocomienzo.login.data.model

data class UserInfo(
    val id: Int,
    val nombre: String,
    val apellidos: String,
    val email: String,
    val password : String,
    val tipo_user: String,
    val id_divece: Number
)