package com.example.nuevocomienzo.log.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nuevocomienzo.log.data.model.LoginDTO
import com.example.nuevocomienzo.log.domain.LoginUseCase
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val loginUseCase = LoginUseCase()

    private val _username = MutableLiveData("")
    val username: LiveData<String> = _username

    private val _password = MutableLiveData("")
    val password: LiveData<String> = _password

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState

    private val _fcmToken = MutableLiveData<String>()
    val fcmToken: LiveData<String> get() = _fcmToken

    private val _installationId = MutableLiveData<String>()
    val installationId: LiveData<String> get() = _installationId

    fun onChangeUsername(value: String) {
        _username.value = value
    }

    fun onChangePassword(value: String) {
        _password.value = value
    }

    fun login() {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            val username = _username.value ?: ""
            val password = _password.value ?: ""

            loginUseCase(username, password)
                .onSuccess { response ->
                    _loginState.value = LoginState.Success(response)
                    println(response.data)
                }
                .onFailure { exception ->
                    _loginState.value = LoginState.Error(exception.message ?: "Error desconocido")
                }
        }
    }
    
}

sealed class LoginState {
    object Initial : LoginState()
    object Loading : LoginState()
    data class Success(val data: LoginDTO) : LoginState()
    data class Error(val message: String) : LoginState()
}