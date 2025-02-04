package com.example.nuevocomienzo.home.presentation


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nuevocomienzo.home.data.model.Product
import com.example.nuevocomienzo.home.data.model.ProductDTO


@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {
    val backgroundColor = Color(0xFFF5F5F7)
    val primaryColor = Color(0xFF000000)
    val secondaryColor = Color(0xFF86868B)
    val accentColor = Color(0xFF007AFF)
    val surfaceColor = Color(0xFFFFFFFF)

    var showAddProductDialog by remember { mutableStateOf(false) }

    // Observe all states from ViewModel
    val products by homeViewModel.products.observeAsState(initial = emptyList())
    val isLoading by homeViewModel.isLoading.observeAsState(initial = false)
    val error by homeViewModel.error.observeAsState(initial = "")
    val productName by homeViewModel.productName.observeAsState(initial = "")
    val productPrice by homeViewModel.productPrice.observeAsState(initial = "")

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
            // Header Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Products",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )

                IconButton(
                    onClick = { showAddProductDialog = true },
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(accentColor)
                        .size(44.dp)
                ) {
                    Icon(
                        Icons.Rounded.Add,
                        contentDescription = "Add Product",
                        tint = Color.White
                    )
                }
            }

            // Error message
            if (error.isNotEmpty()) {
                Text(
                    text = error,
                    color = Color.Red,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Loading indicator
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = accentColor)
                }
            } else {
                // Products Grid
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 160.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(products) { product ->
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

        // Add Product Dialog
        if (showAddProductDialog) {
            AddProductDialog(
                productName = productName,
                productPrice = productPrice,
                onNameChange = { homeViewModel.onChangeProductName(it) },
                onPriceChange = { homeViewModel.onChangeProductPrice(it) },
                onDismiss = { showAddProductDialog = false },
                onAddProduct = {
                    homeViewModel.createProduct()
                    showAddProductDialog = false
                },
                accentColor = accentColor,
                surfaceColor = surfaceColor,
                primaryColor = primaryColor,
                secondaryColor = secondaryColor
            )
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
            .height(200.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Product Image Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE5E5E5)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.Image,
                    contentDescription = null,
                    tint = secondaryColor,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Product Info
            Text(
                text = product.name,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = primaryColor
            )
            Text(
                text = "$${product.price}",
                fontSize = 15.sp,
                color = secondaryColor
            )
        }
    }
}

@Composable
private fun AddProductDialog(
    productName: String,
    productPrice: String,
    onNameChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onAddProduct: () -> Unit,
    accentColor: Color,
    surfaceColor: Color,
    primaryColor: Color,
    secondaryColor: Color
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = surfaceColor,
        title = {
            Text(
                "Add New Product",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = productName,
                    onValueChange = onNameChange,
                    label = { Text("Product Name", color = secondaryColor) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = Color(0xFFE5E5E5)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = productPrice,
                    onValueChange = onPriceChange,
                    label = { Text("Price", color = secondaryColor) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = Color(0xFFE5E5E5)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onAddProduct,
                colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Add Product")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = secondaryColor)
            }
        }
    )
}