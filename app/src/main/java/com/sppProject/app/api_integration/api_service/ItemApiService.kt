package com.sppProject.app.api_integration.api_service
import com.sppProject.app.data.data_class.Item
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ItemApiService {
    @GET("items")
    suspend fun getAllItems(): List<Item>

    @GET("items")
    suspend fun getItemById(id: Long): Item

    @POST("items/company/{compId}")
    suspend fun addItem(
        @Path("compId") compId: Long,
        @Body item: Item
    ): Item
}