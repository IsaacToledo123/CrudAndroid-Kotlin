package com.example.nuevocomienzo.core.network

import com.example.nuevocomienzo.home.data.dataSource.ProductService
import com.example.nuevocomienzo.seller.data.dataSource.ProductServices
import com.example.nuevocomienzo.registro.data.dataSource.RegisterService
import com.example.nuevocomienzo.login.data.dataSource.LoginService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private const val BASE_URL = "https://api-movil.piweb.lat"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val registerService: RegisterService by lazy {
        retrofit.create(RegisterService::class.java)
    }

    val productService: ProductService by lazy {
        retrofit.create(ProductService::class.java)
    }

    val productServices: ProductServices by lazy {
        retrofit.create(ProductServices::class.java)
    }

    val loginService: LoginService by lazy {
        retrofit.create(LoginService::class.java)
    }

}