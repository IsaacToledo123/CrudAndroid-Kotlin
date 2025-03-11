package com.example.nuevocomienzo.home.data.model

data class SubscribeRequest(
    val tipo: String,
    val topic: String,
    val id_user:Int,
    val id_product:Int
)
