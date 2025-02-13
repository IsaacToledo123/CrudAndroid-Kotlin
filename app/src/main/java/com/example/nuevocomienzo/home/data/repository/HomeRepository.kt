package com.example.nuevocomienzo.home.data.repository

import com.example.nuevocomienzo.core.network.RetrofitHelper
import com.example.nuevocomienzo.home.data.model.CreateProductRequest
import com.example.nuevocomienzo.home.data.model.ProductDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeRepository {
    private val homeService = RetrofitHelper.productService

    suspend fun getProducts(): Result<List<ProductDTO>> {
        return try {
            val response = homeService.getProducts()
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createProduct(request: CreateProductRequest): Result<ProductDTO> {
        return try {
            val response = homeService.createProduct(request)
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