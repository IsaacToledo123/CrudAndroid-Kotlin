package com.example.nuevocomienzo.seller.data.dataSource

import com.example.nuevocomienzo.seller.data.model.RequestAddProduct
import com.example.nuevocomienzo.seller.data.model.RequestProduct
import com.example.nuevocomienzo.seller.data.model.ResponseAddProduct
import com.example.nuevocomienzo.seller.data.model.ResponseCreateProduct
import com.example.nuevocomienzo.seller.data.model.ResponseProduct
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductServices {
    @GET("/product/user/{id}")
    suspend fun getProductById(@Path("id") productId: Int): Response<ResponseProduct>

    @POST("/product")
    suspend fun createProduct(@Body request: RequestProduct): Response<ResponseCreateProduct>

    @PUT("/product")
    suspend fun addProduct(@Body request: RequestAddProduct): Response<ResponseAddProduct>
}