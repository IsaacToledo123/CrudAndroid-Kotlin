package com.example.nuevocomienzo.seller.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nuevocomienzo.seller.data.model.ProductDTO
import com.example.nuevocomienzo.seller.data.model.RequestAddProduct
import com.example.nuevocomienzo.seller.domain.AddProductUseCase
import com.example.nuevocomienzo.seller.domain.CreateProductUseCase
import com.example.nuevocomienzo.seller.domain.GetAllProductsUseCase
import kotlinx.coroutines.launch

class SellerViewModel : ViewModel() {
    private val getByIdUserProducts = GetAllProductsUseCase()
    private val createProductUseCase = CreateProductUseCase()
    private val addProductUseCase = AddProductUseCase()

    private val _id = MutableLiveData<Int>()
    val id: LiveData<Int> = _id

    private val _idUser = MutableLiveData<Int>()
    val idUser: LiveData<Int> = _idUser

    private val _cantidad = MutableLiveData<Int>()
    val cantidad: LiveData<Int> = _cantidad

    private val _products = MutableLiveData<List<ProductDTO>?>()
    val products: LiveData<List<ProductDTO>?> = _products

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun onChangeCantidad(cantidad: Int) {
        _cantidad.value = cantidad
    }

    fun addProduct(request: RequestAddProduct, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            addProductUseCase(request).fold(
                onSuccess = {
                    val currentList = _products.value?.toMutableList() ?: mutableListOf()
                    val productIndex = currentList.indexOfFirst { it.id == request.id }

                    if (productIndex != -1) {
                        val currentProduct = currentList[productIndex]
                        val newQuantity = currentProduct.cantidad + request.cantidad.toInt()

                        val updatedProduct = currentProduct.copy(cantidad = newQuantity)

                        currentList[productIndex] = updatedProduct

                        _products.value = currentList
                    } else {
                        idUser.value?.let { loadProducts(it) }
                    }

                    _error.value = ""
                    onSuccess()
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Error desconocido"
                }
            )
            _isLoading.value = false
        }
    }

    fun loadProducts(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            getByIdUserProducts(id).fold(
                onSuccess = { productList ->
                    _products.value = productList.data
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