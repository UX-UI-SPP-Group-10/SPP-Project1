package integration

import com.sppProject.app.api_integration.RetrofitClient
import com.sppProject.app.api_integration.api_service.CompanyApiService
import com.sppProject.app.api_integration.fetchers.CompanyFetcher
import org.junit.Test

class CompanyFetchTest {
    @Test
    fun testFetchCompanies() {
        val companyFetcher =
            CompanyFetcher(RetrofitClient.createApiService(CompanyApiService::class.java))


    }
}