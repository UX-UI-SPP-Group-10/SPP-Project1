package com.sppProject.app

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sppProject.app.api_integration.RetrofitClient
import com.sppProject.app.api_integration.api_service.BuyerApiService
import com.sppProject.app.api_integration.api_service.ItemApiService
import com.sppProject.app.api_integration.api_service.ReceiptApiService
import com.sppProject.app.api_integration.fetchers.BuyerFetcher
import com.sppProject.app.api_integration.fetchers.ItemFetcher
import com.sppProject.app.data.data_class.Receipt
import com.sppProject.app.data.data_class.Buyer
import com.sppProject.app.data.data_class.Item
import com.sppProject.app.api_integration.fetchers.ReceiptFetcher
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ReceiptFetcherIntegrationTest {

    private lateinit var receiptFetcher: ReceiptFetcher
    private lateinit var itemFetcher: ItemFetcher
    private lateinit var buyerFetcher: BuyerFetcher

    @Before
    fun setUp() {
        println("Setting up receipt fetch test")
        // Initialize Retrofit and API Service
        receiptFetcher = ReceiptFetcher(RetrofitClient.createApiService(ReceiptApiService::class.java))
        itemFetcher = ItemFetcher(RetrofitClient.createApiService(ItemApiService::class.java))
        buyerFetcher = BuyerFetcher(RetrofitClient.createApiService(BuyerApiService::class.java))
    }

    @Test
    fun creatingReceiptTest() = runBlocking {
        println("Starting testCreateReceipt")

        // Arrange
        val buyerId = 1L  // Use the existing Buyer ID in the database
        val itemId = 1L   // Use the existing Item ID in the database

        // Act
        val createdReceipt = receiptFetcher.createReceipt(buyerId, itemId)

        // Assert
        assertNotNull("The created receipt should not be null.", createdReceipt)
        assertNotNull("The created receipt ID should not be null.", createdReceipt.id)
    }


    @Test
    fun gettingReceiptsTest() = runBlocking {
        println("Test: Getting list of receipts")
        // Act
        val receipts = receiptFetcher.fetchReceipts()

        // Assert
        assertFalse("The receipt list should not be empty.", receipts.isEmpty())
    }

    @Test
    fun gettingReceiptByIdTest() = runBlocking {
        println("Test: Getting receipt by ID")
        // Arrange
        val testReceiptId = 1L  // Replace with an existing receipt ID or mock

        // Act
        val receipt = receiptFetcher.fetchReceiptById(testReceiptId)

        // Assert
        assertNotNull("The receipt should not be null.", receipt)
        assertEquals("The receipt ID should match the requested ID.", testReceiptId, receipt.id)
    }

    @Test
    fun gettingReceiptByBuyerIdTest() = runBlocking {
        println("Test: Getting receipt by Buyer ID")
        // Arrange
        val testBuyerId = 1L  // Replace with an existing buyer ID or

        // Act
        val receipts = receiptFetcher.fetchReceiptByBuyerId(testBuyerId)

        // Assert
        assertFalse("The receipt list should not be empty.", receipts.isEmpty())
    }

    @After
    fun tearDown() {
        // Perform any cleanup if necessary
        // This could include deleting the created receipt if your API supports it
        // Consider implementing a method to delete a receipt for cleanup
    }
}
