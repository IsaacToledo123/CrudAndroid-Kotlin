package com.example.nuevocomienzo.seller.data.model

import com.example.nuevocomienzo.seller.data.model.ProductDTO

data class ResponseProduct (
    val status: String,
    val data: List<ProductDTO>
)