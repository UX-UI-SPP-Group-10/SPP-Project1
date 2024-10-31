package com.sppProject.app.api_integration.api_service

import com.sppProject.app.api_integration.data_class.Company
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CompanyApiService {
    @GET("companies")
    suspend fun getCompanyById(id: Long): Company

    @GET("companies")
    suspend fun getAllCompanies(): List<Company>

    @POST("companies")
    suspend fun addCompany(@Body newCompany: Company): Company
}