package com.sppProject.app.model.api_integration.api_service

import com.sppProject.app.model.data.data_class.Receipt
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReceiptApiService {
    @GET("receipts")
    suspend fun getAllReceipts(): List<Receipt>

    @GET("receipts/{id}")
    suspend fun getReceiptById(@Path("id") id: Long): Receipt

    @GET("receipts/buyer/{buyerId}")
    suspend fun getReceiptByBuyerId(@Path("buyerId") buyerId: Long): List<Receipt>


    // Updated to use query parameters instead of a JSON body
    @POST("receipts")
    suspend fun createReceipt(
        @Query("buyerId") buyerId: Long,
        @Query("itemId") itemId: Long
    ): Receipt
}
