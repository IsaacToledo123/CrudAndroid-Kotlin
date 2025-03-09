package com.example.nuevocomienzo.seller.data.model

data class RequestProduct (
    val id: Int = 0,
    val name: String,
    val costo: Double,
    val cantidad: Double,
    val imageUrl: String?,
    val idUser: Int
)