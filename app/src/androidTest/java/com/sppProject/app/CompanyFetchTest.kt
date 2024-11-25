package integration

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sppProject.app.model.api_integration.RetrofitClient
import com.sppProject.app.model.api_integration.api_service.CompanyApiService
import com.sppProject.app.model.data.data_class.Company
import com.sppProject.app.model.api_integration.fetchers.CompanyFetcher
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CompanyFetcherIntegrationTest {

    private lateinit var companyFetcher: CompanyFetcher

    @Before
    fun setUp() {
        println("Setting up company fetch test")
        // Initialize Retrofit and API Service
        val companyApiService = RetrofitClient.createApiService(CompanyApiService::class.java)
        companyFetcher = CompanyFetcher(companyApiService)
    }

    @Test
    fun addNewCompanyTest() = runBlocking {
        println("Starting testCreateCompany")
        // Arrange
        val newCompany = Company(id = 0, name = "Test Company")

        // Act
        val createdCompany = companyFetcher.createCompany(newCompany)

        // Assert
        assertEquals(newCompany.name, createdCompany.name)
        assertTrue("The created company ID should be greater than 0.", (createdCompany.id ?: 0) > 0)
        println("Created company: $createdCompany")
    }

    @Test
    fun getAllCompaniesTest() = runBlocking {
        println("Test: Getting list of companies")
        // Act
        val companies = companyFetcher.fetchCompanies()

        // Assert
        assertFalse("The company list should not be empty.", companies.isEmpty())
        println("Fetched companies: $companies")
    }

    @After
    fun tearDown() {
        // Perform any cleanup if necessary
        // This could include deleting the created company if your API supports it
        // Consider implementing a method to delete a company for cleanup
    }
}
