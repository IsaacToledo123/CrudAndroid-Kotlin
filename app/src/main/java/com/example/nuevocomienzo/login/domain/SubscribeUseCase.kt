package com.example.nuevocomienzo.login.domain

import com.example.nuevocomienzo.login.data.model.SubscribeResponse
import com.example.nuevocomienzo.login.data.repository.FirebaseRepository
import com.example.nuevocomienzo.login.data.repository.LoginRepository

class SubscribeUseCase {
    private val services = FirebaseRepository()
    private val repository = LoginRepository()
    suspend operator fun invoke(id:Int):Result<SubscribeResponse>{
        val token = services.getFirebaseToken()
        val idInstallation = services.getFirebaseInstallationId()

        if (token == null) {
            return Result.failure(Exception("Hubo un problema al obtener el token del dispositivo"))
        }
        if (idInstallation == null){
            return Result.failure(Exception("Hubo un problema al obtener el id del dispositivo"))
        }
        val result = repository.subscribe(id, token,idInstallation)

        return result
    }

}