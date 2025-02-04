package com.example.nuevocomienzo.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nuevocomienzo.home.presentation.HomeScreen
import com.example.nuevocomienzo.home.presentation.HomeViewModel
import com.example.nuevocomienzo.log.presentation.LoginScreen
import com.example.nuevocomienzo.log.presentation.LoginViewModel
import com.example.nuevocomienzo.registro.presentation.RegisterScreen
import com.example.nuevocomienzo.registro.presentation.RegisterViewModel

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Register.route
    ) {
        composable(Register.route) {
            val registerViewModel: RegisterViewModel = viewModel()
            RegisterScreen(
                registerViewModel = registerViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate(Login.route) {
                        popUpTo(Register.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Login.route) {
            val loginViewModel: LoginViewModel = viewModel()
            LoginScreen(
                loginViewModel = loginViewModel,
                onNavigateToRegister = {
                    navController.navigate(Register.route)
                },
                onLoginSuccess = {
                    navController.navigate(Home.route) {
                        popUpTo(Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Home.route) {
            val homeViewModel: HomeViewModel = viewModel()
            HomeScreen(
                homeViewModel = homeViewModel
            )
        }
    }
}