package com.sppProject.app.api_integration.fetchers

import com.sppProject.app.api_integration.ApiFetcher
import com.sppProject.app.api_integration.data_class.Company
import com.sppProject.app.api_integration.api_service.CompanyApiService

class CompanyFetcher (
    private val companyApiFetcher: CompanyApiService)
{
    private val apiFetcher = ApiFetcher<Company>(companyApiFetcher)

    suspend fun fetchCompanies(): List<Company> {
        return apiFetcher.handleApiCallList { companyApiFetcher.getAllCompanies() }
    }

    suspend fun createCompany(newCompany: Company): Company {
        return apiFetcher.handleApiCallSingle { companyApiFetcher.addCompany(newCompany) }

    }

    suspend fun getCompanyById(id: Long): Company {
        return apiFetcher.handleApiCallSingle { companyApiFetcher.getCompanyById(id) }
    }
}