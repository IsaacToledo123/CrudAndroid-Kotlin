package com.example.nuevocomienzo.home.data.dataSource


import com.example.nuevocomienzo.home.data.model.CreateProductRequest
import com.example.nuevocomienzo.home.data.model.ProductDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProductService {
    @GET("/products")
    suspend fun getProducts(): Response<List<ProductDTO>>


    @POST("/products")
    suspend fun createProduct(@Body request: CreateProductRequest): Response<ProductDTO>

}