package com.example.nuevocomienzo.home.data.model

data class OrderInfo(
    val id: String,
    val productName: String,
    val status: String,
    val date: String,
    val productId: String,
    val price: Double,
    val shippingAddress: String? = null,
    val estimatedDelivery: String? = null
)