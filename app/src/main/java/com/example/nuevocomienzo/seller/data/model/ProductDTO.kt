package com.example.nuevocomienzo.seller.data.model

data class ProductDTO(
    val id: Int,
    val name: String,
    val costo: Double,
    val cantidad: Int,
    val url_imagen: String?,
    val id_user: Int
)