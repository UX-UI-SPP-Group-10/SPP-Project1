package com.sppProject.app.model.api_integration.api_service

import com.sppProject.app.model.data.data_class.Buyer
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * This interface defines the API endpoints for managing buyers.
 * It specifies how to get and create buyers using the API.
 */
interface BuyerApiService {
    @GET("buyers")
    suspend fun getAllBuyers(): List<Buyer>

    @GET("buyers")
    suspend fun getBuyerById(id: Long): Buyer

    @GET("buyers")
    suspend fun getBuyerByFirebaseUid(firebaseUid: String): Buyer

    @POST("buyers")
    suspend fun createBuyer(@Body newBuyer: Buyer): Buyer
}
