package com.example.nuevocomienzo.registro.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nuevocomienzo.registro.data.model.CreateUserRequest
import com.example.nuevocomienzo.registro.domain.CreateUserUSeCase
import com.example.nuevocomienzo.registro.domain.UsernameValidateUseCase

import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val usernameUseCase = UsernameValidateUseCase()
    private val createUseCase = CreateUserUSeCase()

    private var _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private var _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private var _success = MutableLiveData<Boolean>(false)
    val success: LiveData<Boolean> = _success

    private var _error = MutableLiveData<String>("")
    val error: LiveData<String> = _error

    private var _isRegistered = MutableLiveData<Boolean>(false)
    val isRegistered: LiveData<Boolean> = _isRegistered

    fun onChangeUsername(username: String) {
        _username.value = username
        _success.value = false
        _error.value = ""
    }

    fun onChangePassword(password: String) {
        _password.value = password
    }

    suspend fun onFocusChanged() {
        viewModelScope.launch {
            if (_username.value.isNullOrBlank()) {
                _error.value = "El username no puede estar vacío"
                _success.value = false
                return@launch
            }

            val result = usernameUseCase()
            result.onSuccess { data ->
                if (!data.success) {
                    _success.value = true
                    _error.value = ""
                } else {
                    _success.value = false
                    _error.value = "El username ya existe"
                }
            }.onFailure { exception ->
                _success.value = false
                _error.value = exception.message ?: "Error al validar username"
            }
        }
    }

    suspend fun onClick(user: CreateUserRequest) {
        viewModelScope.launch {
            if (_username.value.isNullOrBlank() || _password.value.isNullOrBlank()) {
                _error.value = "Todos los campos son requeridos"
                return@launch
            }

            val result = createUseCase(user)
            result.onSuccess { data ->
                _isRegistered.value = true
                _error.value = ""
            }.onFailure { exception ->
                _isRegistered.value = false
                _error.value = exception.message ?: "Error al crear usuario"
            }
        }
    }
}