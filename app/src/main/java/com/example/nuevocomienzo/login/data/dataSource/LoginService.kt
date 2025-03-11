package com.example.nuevocomienzo.login.data.dataSource

import com.example.nuevocomienzo.login.data.model.LoginDTO
import com.example.nuevocomienzo.login.data.model.LoginRequest
import com.example.nuevocomienzo.login.data.model.SubscribeRequest
import com.example.nuevocomienzo.login.data.model.SubscribeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface LoginService {
    @POST("/user/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginDTO>

    @PUT("/user/")
    suspend fun subscribe(@Body request: SubscribeRequest): Response<SubscribeResponse>
}