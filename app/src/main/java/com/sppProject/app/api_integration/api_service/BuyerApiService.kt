package com.api_integration.api_service

import com.api_integration.data_class.Buyer
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BuyerApiService {
    @GET("buyers") // Fetch all buyers
    fun getAllBuyers(): Call<List<Buyer>>

    @POST("buyers") // Create a new buyer
    fun createBuyer(@Body item: Buyer): Call<Buyer>
}