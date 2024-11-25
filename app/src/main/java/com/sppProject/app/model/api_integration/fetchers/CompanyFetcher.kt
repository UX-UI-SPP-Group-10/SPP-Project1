package com.sppProject.app.model.api_integration.fetchers

import com.sppProject.app.model.api_integration.ApiFetcher
import com.sppProject.app.model.api_integration.api_service.CompanyApiService
import com.sppProject.app.model.data.data_class.Company

class CompanyFetcher (
    private val companyApiFetcher: CompanyApiService
)
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