package com.example.nuevocomienzo.seller.presentation.components

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nuevocomienzo.seller.data.model.RequestProduct
import com.example.nuevocomienzo.seller.domain.CreateProductUseCase
import kotlinx.coroutines.launch

class CreateProductViewModel: ViewModel()  {
    private val createProductUseCase = CreateProductUseCase()

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private  val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _success

    private  val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private  val _costo = MutableLiveData<Double>()
    val costo: LiveData<Double> = _costo

    private  val _cantidad = MutableLiveData<Int>()
    val cantidad: LiveData<Int> = _cantidad

    private  val _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String> = _imageUrl

    private  val _idUser = MutableLiveData<Int>()
    val idUser: LiveData<Int> = _idUser

    fun onChangeImageUrl(value: String) {
        _imageUrl.value = value
    }

    fun onChangeCoto(value: Double) {
        _costo.value = value
    }

    fun onChangeCantidad(value: Int) {
        _cantidad.value = value
    }

    fun onChangeName(value: String) {
        _name.value = value
    }

    fun createProduct(request: RequestProduct){
        println("Prooo" + request.id_user)
        viewModelScope.launch {
            createProductUseCase(request).fold(
                onSuccess = {
                    _success.value = true
                    _error.value = ""
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Error desconocido"
                }
            )
        }
    }
}