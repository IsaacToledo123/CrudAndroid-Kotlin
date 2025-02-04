package com.example.nuevocomienzo.home.data.model


data class ProductDTO(
    val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String?
)

data class CreateProductRequest(
    val name: String,
    val price: Double,
    val imageUrl: String?
)