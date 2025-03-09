package com.example.nuevocomienzo.seller.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nuevocomienzo.seller.data.model.ProductDTO
import com.example.nuevocomienzo.seller.data.model.RequestProduct
import com.example.nuevocomienzo.seller.domain.CreateProductUseCase
import com.example.nuevocomienzo.seller.domain.GetAllProductsUseCase
import kotlinx.coroutines.launch

class SellerViewModel : ViewModel() {
    private val getByIdUserProducts = GetAllProductsUseCase()
    private val createProductUseCase = CreateProductUseCase()

    private val _products = MutableLiveData<List<ProductDTO>?>()
    val products: MutableLiveData<List<ProductDTO>?> = _products

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

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

     fun createProduct(request: RequestProduct){
        viewModelScope.launch {
            createProductUseCase(request).fold(
                onSuccess = { product ->
                    val currentList = _products.value?.toMutableList()
                    currentList?.add(product.data)
                    _products.value = currentList
                    _error.value = ""
                },
                        onFailure = { exception ->
                    _error.value = exception.message ?: "Error desconocido"
                }
            )
        }
    }
}