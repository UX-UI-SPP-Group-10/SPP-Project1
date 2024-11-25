package com.sppProject.app.model.api_integration.api_service
import com.sppProject.app.model.data.data_class.Item
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ItemApiService {
    @GET("items")
    suspend fun getAllItems(): List<Item>

    @GET("items/{id}") // Specify the correct URL format here, using {id}
    suspend fun getItemById(@Path("id") id: Long): Item

    @GET("items/company/{compId}")
    suspend fun getItemsByCompany(@Path("compId") compId: Long): List<Item>

    @POST("items/company/{compId}")
    suspend fun addItem(
        @Path("compId") compId: Long,
        @Body item: Item
    ): Item

}