package com.example.nuevocomienzo.login.data.repository

import com.example.nuevocomienzo.core.network.RetrofitHelper
import com.example.nuevocomienzo.login.data.model.LoginRequest
import com.example.nuevocomienzo.login.data.model.LoginDTO
import com.example.nuevocomienzo.login.data.model.SubscribeRequest
import com.example.nuevocomienzo.login.data.model.SubscribeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginRepository {
    private val loginService = RetrofitHelper.loginService

    suspend fun login(email: String, password: String): Result<LoginDTO> {
        return withContext(Dispatchers.IO) {
            try {
                val response = loginService.login(LoginRequest(email, password))
                println(response)
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

    suspend fun subscribe(id:Int, token:String, idInstallation:String):Result<SubscribeResponse>{
        return withContext(Dispatchers.IO) {
            try {
                val response = loginService.subscribe(SubscribeRequest(id, token, idInstallation))
                println(response)
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