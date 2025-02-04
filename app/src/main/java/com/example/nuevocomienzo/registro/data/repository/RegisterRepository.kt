package com.example.nuevocomienzo.registro.data.repository

import com.example.nuevocomienzo.core.network.RetrofitHelper
import com.example.nuevocomienzo.registro.data.model.CreateUserRequest
import com.example.nuevocomienzo.registro.data.model.UserDTO
import com.example.nuevocomienzo.registro.data.model.UsernameValidateDTO

class RegisterRepository()  {
    private val registerService = RetrofitHelper.registerService

    suspend fun validateUsername() : Result<UsernameValidateDTO>  {
        return try {

            val response = registerService.validateUsername()

            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createUser(request : CreateUserRequest) : Result<UserDTO> {
        return try {
            val response = registerService.createUser(request)
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