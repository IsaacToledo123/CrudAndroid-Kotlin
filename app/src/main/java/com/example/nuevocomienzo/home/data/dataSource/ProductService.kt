package com.example.nuevocomienzo.home.data.dataSource


import com.example.nuevocomienzo.home.data.model.CreateProductRequest
import com.example.nuevocomienzo.home.data.model.ProductDTO
import com.example.nuevocomienzo.home.data.model.ProductResponse
import com.example.nuevocomienzo.home.data.model.SubscribeRequest
import com.example.nuevocomienzo.home.data.model.SubscribeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProductService {
    @GET("/product")
    suspend fun getProducts(): Response<ProductResponse>


    @POST("/product")
    suspend fun createProduct(@Body request: CreateProductRequest): Response<ProductDTO>

    @POST("/subscribe/subscribe")
    suspend fun subscribe(@Body request: SubscribeRequest): Response<SubscribeResponse>
}