package com.sppProject.app.model.api_integration

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * This object manages the Retrofit instance for making API calls.
 * It sets up Retrofit with a base URL and a JSON converter.
 */
object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/api/"    // ip address for emulator
    //private const val BASE_URL = "http://127.0.0.1:8080/api/" // ip address for physical device

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Create API service instances for different entities
    fun <T> createApiService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}
