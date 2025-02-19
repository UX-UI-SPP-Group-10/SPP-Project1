package com.sppProject.app.model.api_integration.api_service

import com.sppProject.app.model.data.data_class.Company
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CompanyApiService {
    @GET("companies")
    suspend fun getCompanyById(id: Long): Company

    @GET("companies")
    suspend fun getAllCompanies(): List<Company>

    @GET("companies")
    suspend fun getCompanyByFirebaseUid(firebaseUid: String): Company

    @POST("companies")
    suspend fun addCompany(@Body newCompany: Company): Company
}