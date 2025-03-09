package com.example.nuevocomienzo.seller.data.repository

import com.example.nuevocomienzo.core.network.RetrofitHelper
import com.example.nuevocomienzo.seller.data.model.RequestProduct
import com.example.nuevocomienzo.seller.data.model.ResponseCreateProduct
import com.example.nuevocomienzo.seller.data.model.ResponseProduct

class SellerRepository {
    private val sellerServices = RetrofitHelper.productServices

    suspend fun getByIdUserProducts(id: Int): Result<ResponseProduct>{
       return try {
           val response = sellerServices.getProductById(id)
           if (response.isSuccessful) {
               Result.success(response.body()!!)
           } else {
               Result.failure(Exception(response.errorBody()?.string()))
           }
        }catch (e: Exception){
           println("error: $e")
           Result.failure(e)
        }
    }

    suspend fun createProduct(request:RequestProduct):Result<ResponseCreateProduct>{
       return try {
            val response = sellerServices.createProduct(request)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        }catch (e: Exception){
            println("error: $e")
            Result.failure(e)
        }
    }
}