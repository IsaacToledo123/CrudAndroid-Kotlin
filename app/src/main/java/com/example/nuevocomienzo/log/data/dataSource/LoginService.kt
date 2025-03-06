package com.example.nuevocomienzo.log.data.dataSource

import com.example.nuevocomienzo.log.data.model.LoginDTO
import com.example.nuevocomienzo.log.data.model.LoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("/user/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginDTO>
}