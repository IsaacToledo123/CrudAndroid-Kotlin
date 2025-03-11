package com.example.nuevocomienzo.home.data.model

data class SubscribeDTO(
    val id:Int,
    val topic: String,
    val tipo: String,
    val status: Boolean,
    val estado: String,
    val id_user:Int,
    val id_product:Int
)
