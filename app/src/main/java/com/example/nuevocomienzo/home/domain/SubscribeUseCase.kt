package com.example.nuevocomienzo.home.domain

import com.example.nuevocomienzo.home.data.model.ProductResponse
import com.example.nuevocomienzo.home.data.model.SubscribeRequest
import com.example.nuevocomienzo.home.data.model.SubscribeResponse
import com.example.nuevocomienzo.home.data.repository.HomeRepository

class SubscribeUseCase {
    private val repository = HomeRepository()
    suspend operator fun invoke(request: SubscribeRequest): Result<SubscribeResponse> {
        val result = repository.subscribeNotification(request)

        return result
    }

}