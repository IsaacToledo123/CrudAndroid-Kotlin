package com.example.nuevocomienzo.core.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.nuevocomienzo.home.presentation.HomeScreen
import com.example.nuevocomienzo.home.presentation.HomeViewModel
import com.example.nuevocomienzo.login.data.model.UserInfo
import com.example.nuevocomienzo.login.presentation.LoginScreen
import com.example.nuevocomienzo.login.presentation.LoginViewModel
import com.example.nuevocomienzo.registro.presentation.RegisterScreen
import com.example.nuevocomienzo.registro.presentation.RegisterViewModel
import com.example.nuevocomienzo.seller.presentation.SellerScreen
import com.example.nuevocomienzo.seller.presentation.SellerViewModel
import com.example.nuevocomienzo.seller.presentation.components.CreateProductScreen
import com.example.nuevocomienzo.seller.presentation.components.CreateProductViewModel
import com.example.nuevocomienzo.welcome.WelcomeScreen

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Welcome.route
    ) {
        composable(Welcome.route) {
            WelcomeScreen(
                onNavigateToLogin = {
                    navController.navigate(Login.route)
                },
                onNavigateToRegister = {
                    navController.navigate(Register.route)
                }
            )
        }

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
                onLoginSuccess = { userType ->
                    val user = userType as? UserInfo
                    if (user != null) {

                        val destination = if (user.tipo_user == "Comprador") Home.route
                        else Seller.createRoute(user.id)
                        navController.navigate(destination) {
                            popUpTo(Login.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    } else {
                        Log.e("UserInfoDebug", "Error: userType no es de tipo UserInfo")
                    }
                }

            )
        }

        composable(Home.route) {
            val homeViewModel: HomeViewModel = viewModel(
                factory = viewModelFactory {
                    initializer {
                        HomeViewModel(navController.context)
                    }
                }
            )
            HomeScreen(
                homeViewModel = homeViewModel
            )
        }

        composable(
            route = Seller.BaseRoute + "?userId={userId}",
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            val sellerViewModel: SellerViewModel = viewModel(
                factory = viewModelFactory {
                    initializer {
                        SellerViewModel()
                    }
                }
            )
            SellerScreen(
                sellerViewModel = sellerViewModel,
                userId = userId,
                navController = navController,
            )
        }

        composable(
            route = CreateProduct.baseRoute + "?userId={userId}",
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            val createProductViewModel: CreateProductViewModel = viewModel(
                factory = viewModelFactory {
                    initializer {
                        CreateProductViewModel()
                    }
                }
            )
            CreateProductScreen(
                createProductViewModel = createProductViewModel,
                id = userId,
                navController = navController
            )
        }
    }
}