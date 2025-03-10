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
    val backgroundColor = Color(0xFFF8F9FA)
    val primaryColor = Color(0xFF1E88E5)
    val secondaryColor = Color(0xFF757575)
    val agotadoColor = Color(0xFFf81436)
    val surfaceColor = Color.White

    val products by sellerViewModel.products.observeAsState(initial = emptyList())
    val isLoading by sellerViewModel.isLoading.observeAsState(initial = false)
    val error by sellerViewModel.error.observeAsState(initial = "")

    LaunchedEffect(key1 = Unit) {
        sellerViewModel.loadProducts(1)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(35.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Mis Productos",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor
            )

            Button(
                onClick = {
                    navController.navigate(CreateProduct.createRoute(userId))
                },
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .height(48.dp)
                    .wrapContentWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = "Añadir Nuevo Producto",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    softWrap = true
                )
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

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
                columns = GridCells.Adaptive(minSize = 160.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(products.orEmpty()) { product ->
                    ProductCard(
                        product = product,
                        surfaceColor = surfaceColor,
                        primaryColor = primaryColor,
                        secondaryColor = secondaryColor,
                        color = agotadoColor
                    )
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
    secondaryColor: Color,
    color: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(Color(0xFFE5E5E5)),
                contentAlignment = Alignment.Center
            ) {
                if (!product.url_imagen.isNullOrEmpty()) {
                    AsyncImage(
                        model = "https://api-movil.piweb.lat${product.url_imagen}",
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

            Spacer(modifier = Modifier.height(8.dp))

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = product.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryColor
                    )
                    if(product.cantidad >0){
                        Text(
                            text = "Cantidad: ${product.cantidad}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryColor
                        )
                    }else{
                        Text(
                            text = "Producto agotado",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = color
                        )
                    }
                }

                Text(
                    text = "$${product.costo}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2E7D32),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
