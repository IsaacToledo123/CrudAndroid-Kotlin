package com.example.nuevocomienzo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.nuevocomienzo.core.navigation.NavigationWrapper
import com.example.nuevocomienzo.home.presentation.HomeScreen
import com.example.nuevocomienzo.home.presentation.HomeViewModel
import com.example.nuevocomienzo.registro.presentation.RegisterScreen
import com.example.nuevocomienzo.registro.presentation.RegisterViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavigationWrapper()
        }
    }
}

@Composable
fun FakeHomeViewModel(): HomeViewModel {
    return HomeViewModel().apply {
        // You can add some initial fake data here if needed
        // For example:
        // onChangeProductName("Sample Product")
        // onChangeProductPrice("99.99")
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(homeViewModel = FakeHomeViewModel())
}