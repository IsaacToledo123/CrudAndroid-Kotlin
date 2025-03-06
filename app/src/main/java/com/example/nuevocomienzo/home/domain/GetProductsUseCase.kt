package com.example.nuevocomienzo.home.domain

import com.example.nuevocomienzo.home.data.model.CreateProductRequest
import com.example.nuevocomienzo.home.data.model.ProductDTO
import com.example.nuevocomienzo.home.data.repository.HomeRepository

class GetProductsUseCase {
    private val repository = HomeRepository()

    suspend operator fun invoke(): Result<List<ProductDTO>> {
        val result = repository.getProducts()


        return result
    }
}
