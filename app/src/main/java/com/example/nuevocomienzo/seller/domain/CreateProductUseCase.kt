package com.example.nuevocomienzo.seller.domain

import com.example.nuevocomienzo.seller.data.model.RequestProduct
import com.example.nuevocomienzo.seller.data.model.ResponseCreateProduct
import com.example.nuevocomienzo.seller.data.repository.SellerRepository

class CreateProductUseCase {
    private val repository = SellerRepository()

    suspend operator fun invoke(product: RequestProduct): Result<ResponseCreateProduct> {
        if (product.costo <= 0) {
            return Result.failure(Exception("El precio debe ser mayor a 0"))
        }

        if (product.name.isBlank()) {
            return Result.failure(Exception("El nombre no puede estar vacío"))
        }

        val result = repository.createProduct(product)
        return result
    }
}