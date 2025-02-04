package com.example.nuevocomienzo.log.data.repository

import com.example.nuevocomienzo.core.network.RetrofitHelper
import com.example.nuevocomienzo.log.data.model.LoginRequest
import com.example.nuevocomienzo.log.data.model.LoginDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginRepository {
    private val loginService = RetrofitHelper.loginService

    suspend fun login(username: String, password: String): Result<LoginDTO> {
        return withContext(Dispatchers.IO) {
            try {
                val response = loginService.login(LoginRequest(username, password))
                if (response.isSuccessful) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception(response.errorBody()?.string()))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}