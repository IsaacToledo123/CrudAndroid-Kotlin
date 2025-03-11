package com.example.nuevocomienzo.login.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nuevocomienzo.login.data.model.LoginDTO
import com.example.nuevocomienzo.login.data.model.SubscribeResponse
import com.example.nuevocomienzo.login.domain.LoginUseCase
import com.example.nuevocomienzo.login.domain.SubscribeUseCase
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val loginUseCase = LoginUseCase()
    private val subscribeUseCase = SubscribeUseCase()

    private val _username = MutableLiveData("")
    val username: LiveData<String> = _username

    private val _password = MutableLiveData("")
    val password: LiveData<String> = _password

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState

    private val _subscribeState = MutableLiveData<SubscribeResponse>()
    val subscribeState: LiveData<SubscribeResponse> get() = _subscribeState

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
                    subscribe(response.data.id)
                }
                .onFailure { exception ->
                    _loginState.value = LoginState.Error(exception.message ?: "Error desconocido")
                }
        }
    }

    private fun subscribe(id:Int) {
        viewModelScope.launch {
            subscribeUseCase(id)
                .onSuccess { response ->
                    _subscribeState.value = response
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