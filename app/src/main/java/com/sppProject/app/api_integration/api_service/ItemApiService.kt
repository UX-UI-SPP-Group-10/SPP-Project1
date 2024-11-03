package com.sppProject.app.api_integration.api_service
import com.sppProject.app.api_integration.data_class.Item
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ItemApiService {
    @GET("items")
    suspend fun getAllItems(): List<Item>

    @GET("items")
    suspend fun getItemById(id: Long): Item

    @POST("items")
    suspend fun addItem(@Body newItem: Item): Item
}