package com.example.nuevocomienzo.home.data.model

data class ProductResponse(
    val status: String,
    val data: List<ProductDTO>
)

data class ProductDTO(
    val id: String,
    val name: String,
    val costo: Double,
    val cantidad: Double,
    val imageUrl: String?,
    val idUser: String

)

data class CreateProductRequest(
    val name: String,
    val price: Double,
    val imageUrl: String?
)