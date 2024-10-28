package com.api_integration

import com.api_integration.api_service.BuyerApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/api/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Create API service instances
    val buyerApiService: BuyerApiService = retrofit.create(BuyerApiService::class.java)
}
