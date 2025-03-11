package com.example.nuevocomienzo.registro.presentation

import android.util.Patterns
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

    private var _nombre = MutableLiveData<String>()
    val nombre: LiveData<String> = _nombre

    private var _apellidos = MutableLiveData<String>()
    val apellidos: LiveData<String> = _apellidos

    private var _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private var _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private var _tipoUser = MutableLiveData<String>()
    val tipo_user: LiveData<String> = _tipoUser

    private var _success = MutableLiveData<Boolean>(false)
    val success: LiveData<Boolean> = _success

    private var _error = MutableLiveData<String>("")
    val error: LiveData<String> = _error

    private var _isRegistered = MutableLiveData<Boolean>(false)
    val isRegistered: LiveData<Boolean> = _isRegistered

    fun onChangeNombre(nombre: String) {
        _nombre.value = nombre
    }

    fun onChangeApellidos(apellidos: String) {
        _apellidos.value = apellidos
    }

    fun onChangeEmail(email: String) {
        _email.value = email
    }

    fun onChangePassword(password: String) {
        _password.value = password
    }

    fun onChangeTipoUser(tipoUser: String) {
        _tipoUser.value = tipoUser
    }

    suspend fun onFocusChanged() {
        viewModelScope.launch {

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
            if (_password.value.isNullOrBlank() ||
                _nombre.value.isNullOrBlank() ||
                _apellidos.value.isNullOrBlank() ||
                _email.value.isNullOrBlank() ||
                _tipoUser.value.isNullOrBlank()) {
                _error.value = "Todos los campos son requeridos"
                return@launch
            }

            if (!isValidEmail(_email.value!!)) {
                _error.value = "El formato del email no es válido"
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

    private fun isValidEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }
}