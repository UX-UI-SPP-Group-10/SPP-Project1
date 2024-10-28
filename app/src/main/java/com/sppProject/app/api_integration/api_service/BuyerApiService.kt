package com.sppProject.app.api_integration.api_service

import com.sppProject.app.api_integration.data_class.Buyer
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BuyerApiService {
    @GET("buyers")
    suspend fun getAllBuyers(): List<Buyer> // Use suspend function

    @POST("buyers")
    suspend fun createBuyer(@Body item: Buyer): Buyer // Use suspend function
}
