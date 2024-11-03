package com.sppProject.app

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sppProject.app.api_integration.RetrofitClient
import com.sppProject.app.api_integration.api_service.ItemApiService
import com.sppProject.app.api_integration.data_class.Company
import com.sppProject.app.api_integration.data_class.Item
import com.sppProject.app.api_integration.fetchers.ItemFetcher
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ItemFetcherIntegrationTest {

    private lateinit var itemFetcher: ItemFetcher

    @Before
    fun setUp() {
        println("Setting up item fetch test")
        // Initialize Retrofit and API Service
        itemFetcher = ItemFetcher(RetrofitClient.createApiService(ItemApiService::class.java))
    }

    @Test
    fun creatingItemTest() = runBlocking {
        println("Starting testCreateItem")
        // Arrange
        val company = Company(id = 1, name = "Test Company") // Assume this company already exists in backend
        val newItem = Item(
            name = "Test Item",
            price = 100,
            description = "A test item description",
            stock = 10,
            company = company
        )

        // Act
        val createdItem = itemFetcher.createItem(newItem)

        // Assert
        assertEquals(newItem.name, createdItem.name)
        assertEquals(newItem.price, createdItem.price)
        assertEquals(newItem.description, createdItem.description)
        assertEquals(newItem.stock, createdItem.stock)
        assertEquals(newItem.company.id, createdItem.company.id)
        assertTrue("The created item ID should be greater than 0.", (createdItem.id ?: 0) > 0)
        println("Created item: $createdItem")
    }

    @Test
    fun gettingItemsTest() = runBlocking {
        println("Test: Getting list of items")
        // Act
        val items = itemFetcher.fetchItems()

        // Assert
        assertFalse("The item list should not be empty.", items.isEmpty())
        println("Fetched items: $items")
    }

    @After
    fun tearDown() {
        // Perform any cleanup if necessary
        // This could include deleting the created item if your API supports it
        // Consider implementing a method to delete an item for cleanup
    }
}
