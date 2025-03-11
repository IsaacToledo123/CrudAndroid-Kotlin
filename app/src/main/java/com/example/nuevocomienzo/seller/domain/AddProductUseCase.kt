package com.example.nuevocomienzo.seller.domain

import com.example.nuevocomienzo.seller.data.model.RequestAddProduct
import com.example.nuevocomienzo.seller.data.model.ResponseAddProduct
import com.example.nuevocomienzo.seller.data.repository.SellerRepository

class AddProductUseCase {
    private val repository = SellerRepository()
    suspend operator fun invoke(request: RequestAddProduct):Result<ResponseAddProduct>{
        val result = repository.addProduct(request)
        return result
    }
}