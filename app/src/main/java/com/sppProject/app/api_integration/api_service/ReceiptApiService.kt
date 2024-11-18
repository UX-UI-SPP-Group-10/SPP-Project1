package com.sppProject.app.api_integration.api_service

import com.sppProject.app.data.data_class.Receipt
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReceiptApiService {
    @GET("receipts")
    suspend fun getAllReceipts(): List<Receipt>

    @GET("receipts/{id}")
    suspend fun getReceiptById(@Path("id") id: Long): Receipt

    // Updated to use query parameters instead of a JSON body
    @POST("receipts")
    suspend fun createReceipt(
        @Query("buyerId") buyerId: Long,
        @Query("itemId") itemId: Long
    ): Receipt
}
