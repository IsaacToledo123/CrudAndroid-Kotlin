package com.example.nuevocomienzo.home.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nuevocomienzo.home.data.model.CreateProductRequest
import com.example.nuevocomienzo.home.data.model.ProductDTO
import com.example.nuevocomienzo.home.domain.CreateProductUseCase
import com.example.nuevocomienzo.home.domain.GetProductsUseCase
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.example.nuevocomienzo.home.data.model.OrderInfo
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeViewModel(private val context: Context) : ViewModel() {
    private val getProductsUseCase = GetProductsUseCase()
    private val createProductUseCase = CreateProductUseCase()

    private val _products = MutableLiveData<List<ProductDTO>>()
    val products: LiveData<List<ProductDTO>> = _products

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // Nuevo LiveData para los pedidos
    private val _orders = MutableLiveData<List<OrderInfo>>(emptyList())
    val orders: LiveData<List<OrderInfo>> = _orders

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            getProductsUseCase().fold(
                onSuccess = { productList ->
                    _products.value = productList
                    _error.value = ""
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Error desconocido"
                }
            )
            _isLoading.value = false
        }
    }

    private fun vibratePhone() {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                val vibrator = vibratorManager.defaultVibrator
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(50)
                }
            }
        } catch (e: Exception) {
            // Ignorar errores de vibración
        }
    }

    fun onBuyProduct(productId: String) {
        // Vibrar el teléfono al comprar
        vibratePhone()

        // Encontrar el producto por ID
        val product = _products.value?.find { it.id == productId }

        product?.let {
            // Crear un nuevo pedido
            val newOrder = OrderInfo(
                id = "ORD-${System.currentTimeMillis()}",
                productName = it.name,
                status = "Procesando",
                date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
                productId = it.id,
                price = it.costo
            )

            // Añadir el nuevo pedido a la lista existente
            val currentOrders = _orders.value ?: emptyList()
            _orders.value = listOf(newOrder) + currentOrders

            // Aquí irías la lógica para procesar la compra, como llamar a un use case
            // Por ejemplo: processOrderUseCase(newOrder)
        }
    }

    fun onNotifyWhenAvailable(productId: String) {
        // Vibrar el teléfono al solicitar notificación
        vibratePhone()

        // Lógica para registrar la notificación
        // Por ejemplo: registerNotificationUseCase(productId)
    }

    // Método para actualizar el estado de un pedido (para propósitos de prueba)
    fun updateOrderStatus(orderId: String, newStatus: String) {
        val currentOrders = _orders.value ?: return
        val updatedOrders = currentOrders.map { order ->
            if (order.id == orderId) {
                order.copy(status = newStatus)
            } else {
                order
            }
        }
        _orders.value = updatedOrders
    }

    fun loadSavedOrders() {

        val sampleOrders = listOf(
            OrderInfo(
                id = "ORD-001",
                productName = "Smartphone XYZ",
                status = "En preparación",
                date = "2025-03-05",
                productId = "1",
                price = 599.99
            ),
            OrderInfo(
                id = "ORD-002",
                productName = "Laptop ABC",
                status = "Enviado",
                date = "2025-03-01",
                productId = "2",
                price = 1299.99
            )
        )

        _orders.value = sampleOrders
    }
}


