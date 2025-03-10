package com.example.nuevocomienzo.core.navigation

import kotlinx.serialization.Serializable

@Serializable
object Welcome {
    val route = "welcome"
}

@Serializable
object Login {
    val route = "login"
}

@Serializable
object Register {
    val route = "register"
}

@Serializable
object Home {
    val route = "home"
}

@Serializable
data class Seller(val userId: Int? = 0) {
    companion object {
        const val BaseRoute = "seller"
        fun createRoute(userId: Int) = "seller?userId=$userId"
    }
}

@Serializable
data class CreateProduct(val userId: Int? = 0) {
    companion object {
        const val baseRoute = "create_product"
        fun createRoute(userId: Int) = "create_product?userId=$userId"
    }
}