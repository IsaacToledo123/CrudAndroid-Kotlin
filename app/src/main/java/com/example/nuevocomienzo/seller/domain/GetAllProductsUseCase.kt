package com.example.nuevocomienzo.seller.domain

import com.example.nuevocomienzo.seller.data.model.ResponseProduct
import com.example.nuevocomienzo.seller.data.repository.SellerRepository

class GetAllProductsUseCase {
    private val repository = SellerRepository()

    suspend operator fun invoke(id:Int):Result<ResponseProduct>{
        val result = repository.getByIdUserProducts(id)

        return result
    }
}