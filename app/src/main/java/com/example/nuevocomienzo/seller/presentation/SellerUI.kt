package com.example.nuevocomienzo.seller.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.nuevocomienzo.core.navigation.CreateProduct
import com.example.nuevocomienzo.seller.data.model.ProductDTO

@Composable
fun SellerScreen(
    sellerViewModel: SellerViewModel,
    userId: Int,
    navController: NavController
) {
    val backgroundColor = Color(0xFFF5F5F7)
    val primaryColor = Color(0xFF000000)
    val secondaryColor = Color(0xFF86868B)
    val surfaceColor = Color(0xFFFFFFFF)

    val products by sellerViewModel.products.observeAsState(initial = emptyList())
    val isLoading by sellerViewModel.isLoading.observeAsState(initial = false)
    val error by sellerViewModel.error.observeAsState(initial = "")

    LaunchedEffect(key1 = Unit) {
        sellerViewModel.loadProducts(userId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mis Productos",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )

                // Botón para añadir un nuevo producto
                Button(
                    onClick = {
                        navController.navigate(CreateProduct.createRoute(userId))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                ) {
                    Text(
                        text = "Añadir Producto",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            if (error.isNotEmpty()) {
                Text(
                    text = error,
                    color = Color.Red,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = primaryColor)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 180.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(products.orEmpty()) { product ->
                        ProductCard(
                            product = product,
                            surfaceColor = surfaceColor,
                            primaryColor = primaryColor,
                            secondaryColor = secondaryColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductCard(
    product: ProductDTO,
    surfaceColor: Color,
    primaryColor: Color,
    secondaryColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFFE5E5E5)),
                contentAlignment = Alignment.Center
            ) {
                if (!product.url_imagen.isNullOrEmpty()) {
                    AsyncImage(
                        model = "https://api-movil.piweb.lat" + product.url_imagen,
                        contentDescription = product.name,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text(
                        text = "Imagen no disponible",
                        color = secondaryColor,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = product.name,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = primaryColor
            )

            Text(
                text = "$${product.costo}",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor
            )
        }
    }
}