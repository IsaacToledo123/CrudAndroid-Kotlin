package com.example.nuevocomienzo.seller.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
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
import com.example.nuevocomienzo.seller.data.model.RequestAddProduct
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.coroutines.delay

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
    val successColor = Color(0xFF4CAF50)

    val products by sellerViewModel.products.observeAsState(initial = emptyList())
    val isLoading by sellerViewModel.isLoading.observeAsState(initial = false)
    val error by sellerViewModel.error.observeAsState(initial = "")
    var showAddStockDialog by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<ProductDTO?>(null) }
    var showSuccessMessage by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }

    LaunchedEffect(showSuccessMessage) {
        if (showSuccessMessage) {
            delay(3000)
            showSuccessMessage = false
        }
    }

    LaunchedEffect(key1 = Unit) {
        sellerViewModel.loadProducts(userId)
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

        AnimatedVisibility(
            visible = showSuccessMessage,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = successColor.copy(alpha = 0.1f)),
                border = BorderStroke(1.dp, successColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Éxito",
                        tint = successColor
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = successMessage,
                        color = successColor,
                        fontWeight = FontWeight.Medium
                    )
                }
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
                        color = agotadoColor,
                        onAddStock = {
                            selectedProduct = it
                            showAddStockDialog = true
                        }
                    )
                }
            }
        }
    }

    if (showAddStockDialog && selectedProduct != null) {
        AddStockDialog(
            product = selectedProduct!!,
            onDismiss = { showAddStockDialog = false },
            onAddStock = { productId, cantidad ->
                sellerViewModel.addProduct(
                    RequestAddProduct(
                        id = productId,
                        cantidad = cantidad
                    ),
                    onSuccess = {
                        val productName = selectedProduct?.name ?: "Producto"
                        successMessage = "¡Se han añadido $cantidad unidades al inventario de $productName!"
                        showSuccessMessage = true
                    }
                )
                showAddStockDialog = false
            }
        )
    }
}

@Composable
private fun ProductCard(
    product: ProductDTO,
    surfaceColor: Color,
    primaryColor: Color,
    secondaryColor: Color,
    color: Color,
    onAddStock: (ProductDTO) -> Unit
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
                        color = primaryColor,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$${product.costo}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2E7D32),
                    )

                    if(product.cantidad > 0) {
                        Text(
                            text = "Cantidad: ${product.cantidad}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryColor
                        )
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Agotado",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = color,
                                modifier = Modifier.padding(end = 4.dp)
                            )

                            Button(
                                onClick = { onAddStock(product) },
                                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .height(30.dp)
                                    .wrapContentWidth()
                            ) {
                                Text(
                                    text = "Añadir",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddStockDialog(
    product: ProductDTO,
    onDismiss: () -> Unit,
    onAddStock: (Int, Int) -> Unit
) {
    var stockAmount by remember { mutableStateOf("") }
    val isError = remember { mutableStateOf(false) }
    val primaryColor = Color(0xFF1E88E5)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Añadir existencias",
                fontWeight = FontWeight.Bold,
                color = primaryColor
            )
        },
        text = {
            Column {
                Text(
                    text = "Producto: ${product.name}",
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Precio: $${product.costo}",
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = stockAmount,
                    onValueChange = {
                        if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                            stockAmount = it
                            isError.value = false
                        }
                    },
                    label = { Text("Cantidad a añadir") },
                    isError = isError.value,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                if (isError.value) {
                    Text(
                        text = "Por favor, ingresa una cantidad válida",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (stockAmount.isNotEmpty() && stockAmount.toIntOrNull() != null && stockAmount.toInt() > 0) {
                        onAddStock(product.id, stockAmount.toInt())
                        onDismiss()
                    } else {
                        isError.value = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
