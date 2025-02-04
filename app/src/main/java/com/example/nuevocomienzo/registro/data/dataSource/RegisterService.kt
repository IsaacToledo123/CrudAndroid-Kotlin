package com.example.nuevocomienzo.registro.data.dataSource

import com.example.nuevocomienzo.registro.data.model.CreateUserRequest
import com.example.nuevocomienzo.registro.data.model.UserDTO
import com.example.nuevocomienzo.registro.data.model.UsernameValidateDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RegisterService {

    @GET("/users/validate")
    suspend fun validateUsername() : Response<UsernameValidateDTO>

    @POST("users")
    suspend fun createUser(@Body request : CreateUserRequest) : Response<UserDTO>


}