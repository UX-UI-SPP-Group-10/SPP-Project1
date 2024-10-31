package com.sppProject.app.api_integration.api_service

import com.sppProject.app.api_integration.data_class.Company
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CompanyApiService {
    @GET("company")
    suspend fun getCompany(): Company
    @POST("company")
    suspend fun addCompany(@Body company: Company)
}