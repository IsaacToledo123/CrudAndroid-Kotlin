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