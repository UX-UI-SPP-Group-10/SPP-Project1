package com.sppProject.app

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.navigation.NavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sppProject.app.view.UserHomePage
import com.sppProject.app.viewModel.UserViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import androidx.navigation.compose.rememberNavController
import com.sppProject.app.UserNavActions
import com.sppProject.app.model.api_integration.RetrofitClient
import com.sppProject.app.model.api_integration.api_service.BuyerApiService
import com.sppProject.app.model.api_integration.api_service.CompanyApiService
import com.sppProject.app.model.api_integration.api_service.ItemApiService
import com.sppProject.app.model.api_integration.fetchers.BuyerFetcher
import com.sppProject.app.model.api_integration.fetchers.CompanyFetcher
import com.sppProject.app.model.api_integration.fetchers.ItemFetcher
import com.sppProject.app.model.data.UserSessionManager
import com.sppProject.app.model.data.data_class.Item

// Database needs to run, otherwise emulator is seen as offline
@RunWith(AndroidJUnit4::class)
class UserViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var usernavController: NavHostController
    private lateinit var userNavActions: UserNavActions

    // Fake API to store data locally, and only test the user home view
    class FakeItemApiService : ItemApiService {
        private val items = mutableListOf<Item>()

        override suspend fun getAllItems(): List<Item> = items

        override suspend fun addItem(compId: Long, newItem: Item): Item {
            newItem.id = (items.size + 1).toLong()
            items.add(newItem)
            return newItem
        }

        override suspend fun updateItem(id: Long, updatedItem: Item): Item {
            TODO("Not yet implemented")
        }

        override suspend fun getItemById(id: Long): Item =
            items.first { it.id == id }

        override suspend fun getItemsByCompany(compId: Long): List<Item> =
            items.filter { it.id == compId }
    }

    @Test
    fun userViewTest() {
        runBlocking {
            val fakeItemApiService = FakeItemApiService()
            val itemFetcher = ItemFetcher(fakeItemApiService)
            val context = ApplicationProvider.getApplicationContext<Context>()
            val userSessionManager = UserSessionManager(context)

            // Adding an item to see if it's shown
            val newItem = Item(
                name = "Test Item",
                price = 100,
                description = "A test item description",
                stock = 10
            )
            fakeItemApiService.addItem(compId = 1, newItem = newItem)

            // Sets up the UI for testing
            composeTestRule.setContent {
                usernavController = rememberNavController()
                userNavActions = UserNavActions(usernavController)

                val userViewModel = UserViewModel(
                    navActions = userNavActions,
                    buyerFetcher = BuyerFetcher(RetrofitClient.createApiService(BuyerApiService::class.java)),
                    companyFetcher = CompanyFetcher(RetrofitClient.createApiService(CompanyApiService::class.java)),
                    userSessionManager = userSessionManager
                )

                UserHomePage(
                    navActions = userNavActions,
                    userViewModel = userViewModel,
                    itemFetcher = itemFetcher
                )
            }

            // Waits until the UI is idle
            composeTestRule.waitForIdle()

            // Test to make sure the item was properly created
            assertEquals(newItem.name, newItem.name)
            assertEquals(newItem.price, newItem.price)
            assertEquals(newItem.description, newItem.description)
            assertEquals(newItem.stock, newItem.stock)
            assertTrue("The created item ID should be greater than 0.", (newItem.id ?: 0) > 0)

            // Tests if the item is on the user home page
            composeTestRule.onNodeWithText("Test Item").assertExists()
            composeTestRule.onNodeWithText("Stock: 10").assertExists()
        }
    }

}
