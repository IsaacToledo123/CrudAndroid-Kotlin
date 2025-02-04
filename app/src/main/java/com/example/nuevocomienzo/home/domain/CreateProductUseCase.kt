package com.example.nuevocomienzo.home.domain

import com.example.nuevocomienzo.home.data.model.CreateProductRequest
import com.example.nuevocomienzo.home.data.model.ProductDTO
import com.example.nuevocomienzo.home.data.repository.HomeRepository

class CreateProductUseCase {
    private val repository = HomeRepository()

    suspend operator fun invoke(product: CreateProductRequest): Result<ProductDTO> {
        if (product.price <= 0) {
            return Result.failure(Exception("El precio debe ser mayor a 0"))
        }

        if (product.name.isBlank()) {
            return Result.failure(Exception("El nombre no puede estar vacío"))
        }

        val result = repository.createProduct(product)
        return result
    }
}