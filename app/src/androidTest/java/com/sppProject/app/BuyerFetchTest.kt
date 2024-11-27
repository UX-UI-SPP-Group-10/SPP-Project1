package com.sppProject.app

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sppProject.app.model.api_integration.RetrofitClient
import com.sppProject.app.model.api_integration.api_service.BuyerApiService
import com.sppProject.app.model.data.data_class.Buyer
import com.sppProject.app.model.api_integration.fetchers.BuyerFetcher
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BuyerFetcherIntegrationTest {

    private lateinit var buyerFetcher: BuyerFetcher

    @Before
    fun setUp() {
        println("Setting up buyer fetch test")
        // Initialize Retrofit and API Service
        buyerFetcher = BuyerFetcher(RetrofitClient.createApiService(BuyerApiService::class.java))
    }

    @Test
    fun creatingBuyerTest() = runBlocking {
        println("Starting testCreateBuyer")
        // Arrange
        val newBuyer = Buyer(id = 0, name = "Test User")

        // Act
        val createdBuyer = buyerFetcher.createBuyer(newBuyer)

        // Assert
        assertEquals(newBuyer.name, createdBuyer.name)
        assertTrue("The created buyer ID should be greater than 0.", (createdBuyer.id ?: 0) > 0)
    }

    @Test
    fun gettingBuyersTest() = runBlocking {
        println("Test: Getting list of buyers")
        // Act
        val buyers = buyerFetcher.fetchBuyers()

        // Assert
        assertFalse("The buyer list should not be empty.", buyers.isEmpty())
    }

    @After
    fun tearDown() {
        // Perform any cleanup if necessary
        // This could include deleting the created buyer if your API supports it
        // Consider implementing a method to delete a buyer for cleanup
    }
}
