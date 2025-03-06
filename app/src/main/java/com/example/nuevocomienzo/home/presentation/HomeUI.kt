package com.example.nuevocomienzo.home.presentation


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
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
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.nuevocomienzo.home.data.model.OrderInfo


@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {
    val backgroundColor = Color(0xFFF5F5F7)
    val primaryColor = Color(0xFF000000)
    val secondaryColor = Color(0xFF86868B)
    val accentColor = Color(0xFF007AFF)
    val notificationColor = Color(0xFF34C759)
    val surfaceColor = Color(0xFFFFFFFF)

    val products by homeViewModel.products.observeAsState(initial = emptyList())
    val isLoading by homeViewModel.isLoading.observeAsState(initial = false)
    val error by homeViewModel.error.observeAsState(initial = "")
    val orders by homeViewModel.orders.observeAsState(initial = emptyList())
    var showOrderTrackingModal by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        homeViewModel.loadSavedOrders()
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Productos",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )

                BadgedBox(
                    modifier = Modifier
                        .size(48.dp)
                        .background(surfaceColor, shape = CircleShape),
                    badge = {
                        Badge(
                            containerColor = accentColor
                        ) {
                            Text(
                                text = orders.size.toString(),
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                    }
                ) {
                    IconButton(
                        onClick = { showOrderTrackingModal = true }
                    ) {
                        IconButton(
                            onClick = { showOrderTrackingModal = true },
                            modifier = Modifier
                                .size(48.dp)
                                .background(surfaceColor, shape = CircleShape)
                        ) {
                            Badge(
                                modifier = Modifier
                                    .offset(x = 14.dp, y = (-14).dp)
                                    .align(Alignment.TopEnd),
                                containerColor = accentColor
                            ) {
                                Text(
                                    text = orders.size.toString(),
                                    color = Color.White,
                                    fontSize = 12.sp
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ShoppingBag,
                                contentDescription = "Ver mis pedidos",
                                tint = accentColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
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
                    columns = GridCells.Adaptive(minSize = 180.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(products) { product ->
                        ProductCard(
                            product = product,
                            surfaceColor = surfaceColor,
                            primaryColor = primaryColor,
                            secondaryColor = secondaryColor,
                            accentColor = accentColor,
                            notificationColor = notificationColor,
                            onBuyClick = { productId ->
                                homeViewModel.onBuyProduct(productId)
                                showOrderTrackingModal = true
                            },
                            onNotifyClick = { productId ->
                                homeViewModel.onNotifyWhenAvailable(productId)
                            }
                        )
                    }
                }
            }
        }

        // Modal de seguimiento de pedidos
        if (showOrderTrackingModal) {
            OrderTrackingModal(
                onDismiss = { showOrderTrackingModal = false },
                orders = orders,
                surfaceColor = surfaceColor,
                primaryColor = primaryColor,
                secondaryColor = secondaryColor,
                accentColor = accentColor
            )
        }
    }
}

@Composable
private fun OrderTrackingModal(
    onDismiss: () -> Unit,
    orders: List<OrderInfo>,
    surfaceColor: Color,
    primaryColor: Color,
    secondaryColor: Color,
    accentColor: Color
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = surfaceColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                // Encabezado
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Seguimiento de Pedidos",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryColor
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.AddShoppingCart,
                            contentDescription = "Cerrar",
                            tint = secondaryColor
                        )
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 16.dp),
                    color = Color(0xFFE5E5E5)
                )

                if (orders.isEmpty()) {
                    // Mostrar mensaje si no hay pedidos
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = secondaryColor
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No tienes pedidos activos",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = secondaryColor
                            )
                        }
                    }
                } else {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {


                    }
                }
            }
        }
    }
}

@Composable
private fun OrderCard(
    order: OrderInfo,
    surfaceColor: Color,
    primaryColor: Color,
    secondaryColor: Color,
    accentColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = order.id,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = secondaryColor
                )
                Text(
                    text = order.date,
                    fontSize = 14.sp,
                    color = secondaryColor
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = order.productName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = primaryColor,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "$${order.price}",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Indicador de estado
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Indicador visual según el estado
                val statusColor = when(order.status) {
                    "Procesando" -> Color(0xFF007AFF)
                    "En preparación" -> Color(0xFFFF9500)
                    "Enviado" -> Color(0xFF34C759)
                    "Entregado" -> Color(0xFF34C759)
                    else -> secondaryColor
                }

                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(statusColor, CircleShape)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = order.status,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = statusColor
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Barra de progreso
            val progress = when(order.status) {
                "Procesando" -> 0.25f
                "En preparación" -> 0.5f
                "Enviado" -> 0.75f
                "Entregado" -> 1f
                else -> 0f
            }
            val statusColor = when(order.status) {
                "Procesando" -> Color(0xFF007AFF)
                "En preparación" -> Color(0xFFFF9500)
                "Enviado" -> Color(0xFF34C759)
                "Entregado" -> Color(0xFF34C759)
                else -> secondaryColor
            }

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = statusColor,
                trackColor = Color(0xFFE5E5E5),
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Mostrar estimación de entrega si está disponible
            if (order.estimatedDelivery != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = secondaryColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Entrega estimada: ${order.estimatedDelivery}",
                        fontSize = 14.sp,
                        color = secondaryColor
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Botón para más detalles
            OutlinedButton(
                onClick = { /* No implementado */ },
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = accentColor
                ),
                border = BorderStroke(1.dp, accentColor)
            ) {
                Text("Ver detalles")
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
    accentColor: Color,
    notificationColor: Color,
    onBuyClick: (String) -> Unit,
    onNotifyClick: (String) -> Unit
) {
    // Código del ProductCard (sin cambios)
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
            // Product Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE5E5E5)),
                contentAlignment = Alignment.Center
            ) {
                if (!product.imageUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = product.imageUrl,
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        Icons.Rounded.Image,
                        contentDescription = null,
                        tint = secondaryColor,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = product.name,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = primaryColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$${product.costo}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = primaryColor
                )

                // Mostrar cantidad disponible o agotado
                if (product.cantidad > 0) {
                    Text(
                        text = "Disponibles: ${product.cantidad}",
                        fontSize = 14.sp,
                        color = secondaryColor
                    )
                } else {
                    Text(
                        text = "Agotado",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Red
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (product.cantidad > 0) {
                Button(
                    onClick = { onBuyClick(product.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accentColor,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Icono de compra",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Comprar",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                Button(
                    onClick = { onNotifyClick(product.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = notificationColor,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Icono de notificación",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Avisar disponibilidad",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}