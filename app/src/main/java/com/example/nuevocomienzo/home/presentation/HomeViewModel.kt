package com.example.nuevocomienzo.home.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nuevocomienzo.home.data.model.CreateProductRequest
import com.example.nuevocomienzo.home.data.model.ProductDTO
import com.example.nuevocomienzo.home.domain.CreateProductUseCase
import com.example.nuevocomienzo.home.domain.GetProductsUseCase
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val getProductsUseCase = GetProductsUseCase()
    private val createProductUseCase = CreateProductUseCase()

    private var _products = MutableLiveData<List<ProductDTO>>()
    val products: LiveData<List<ProductDTO>> = _products

    private var _productName = MutableLiveData<String>()
    val productName: LiveData<String> = _productName

    private var _productPrice = MutableLiveData<String>()
    val productPrice: LiveData<String> = _productPrice

    private var _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        loadProducts()
    }

    fun onChangeProductName(name: String) {
        _productName.value = name
    }

    fun onChangeProductPrice(price: String) {
        _productPrice.value = price
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

    fun createProduct() {
        val name = _productName.value ?: ""
        val price = _productPrice.value?.toDoubleOrNull() ?: 0.0

        val request = CreateProductRequest(
            name = name,
            price = price,
            imageUrl = null
        )

        viewModelScope.launch {
            _isLoading.value = true
            createProductUseCase(request).fold(
                onSuccess = { newProduct ->
                    val currentList = _products.value.orEmpty().toMutableList()
                    currentList.add(newProduct)
                    _products.value = currentList

                    // Limpiar campos
                    _productName.value = ""
                    _productPrice.value = ""
                    _error.value = ""
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Error desconocido"
                }
            )
            _isLoading.value = false
        }
    }
}