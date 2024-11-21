package com.sppProject.app

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sppProject.app.api_integration.RetrofitClient
import com.sppProject.app.api_integration.api_service.BuyerApiService
import com.sppProject.app.api_integration.api_service.ItemApiService
import com.sppProject.app.api_integration.api_service.ReceiptApiService
import com.sppProject.app.api_integration.fetchers.BuyerFetcher
import com.sppProject.app.api_integration.fetchers.ItemFetcher
import com.sppProject.app.UserNavActions
import com.sppProject.app.api_integration.api_service.CompanyApiService
import com.sppProject.app.api_integration.fetchers.CompanyFetcher
import com.sppProject.app.view.UserHomePage
import com.sppProject.app.data.data_class.Receipt
import com.sppProject.app.data.data_class.Buyer
import com.sppProject.app.data.data_class.Item
import com.sppProject.app.api_integration.fetchers.ReceiptFetcher
import com.sppProject.app.data.UserSessionManager
import com.sppProject.app.viewModel.UserViewModel
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // private lateinit var itemFetcher: ItemFetcher
    // private lateinit var userNavActions : UserNavActions
    private lateinit var usernavController: NavHostController

        // Set up a fate API in order to not disturb the database
       /*class FakeItemApiService : ItemApiService {
            private val items = mutableListOf<Item>()

            override suspend fun getAllItems(): List<Item> {
                return items
            }

            override suspend fun addItem(compId: Long, newItem: Item): Item {
                // Add item to the list and return it
                newItem.id = (items.size + 1).toLong() // Simulating ID generation
                items.add(newItem)
                return newItem
            }

            override suspend fun getItemById(id: Long): Item {
                return items.first { it.id == id }
            }

            override suspend fun getItemsByCompany(compId: Long): List<Item> {
                return items.filter { it.id == compId }
            }
        }*/

        @Test
        fun userViewTest() = runBlocking {

            // Step 1: Use the fake ItemApiService
            val itemApiService = RetrofitClient.createApiService(ItemApiService::class.java)
            val itemFetcher = ItemFetcher(itemApiService)
            val context = ApplicationProvider.getApplicationContext<Context>()
            val userSessionManager = UserSessionManager(context)

            composeTestRule.setContent {
                // Initialize and render UserHomePage
                usernavController = rememberNavController()
                val userSessionManager = UserSessionManager(ApplicationProvider.getApplicationContext())
                val userViewModel = UserViewModel(
                    navActions = UserNavActions(usernavController),
                    buyerFetcher = BuyerFetcher(RetrofitClient.createApiService(BuyerApiService::class.java)),
                    companyFetcher = CompanyFetcher(RetrofitClient.createApiService(CompanyApiService::class.java)),
                    userSessionManager = userSessionManager
                )

                UserHomePage(
                    navActions = UserNavActions(usernavController),
                    userViewModel = userViewModel,
                    itemFetcher = itemFetcher
                )
            }

            /*val userViewModel = UserViewModel(navActions = UserNavActions(usernavController),
                buyerFetcher = BuyerFetcher(RetrofitClient.createApiService(BuyerApiService::class.java)),
                companyFetcher = CompanyFetcher(RetrofitClient.createApiService(CompanyApiService::class.java)),
                userSessionManager = userSessionManager )*/

            val newItem = Item(
                name = "Test Item",
                price = 100,
                description = "A test item description",
                stock = 10
            )

            // Add the item using the fake ItemFetcher
            val createdItemResponse = itemFetcher.createItem(compId = 1, newItem = newItem)

            assertEquals(newItem.name, createdItemResponse.name)
            assertEquals(newItem.price, createdItemResponse.price)
            assertEquals(newItem.description, createdItemResponse.description)
            assertEquals(newItem.stock, createdItemResponse.stock)
            assertTrue("The created item ID should be greater than 0.", createdItemResponse.id!! > 0)

            // Step 2: Set up the Compose test environment
            /*composeTestRule.setContent {
                usernavController = rememberNavController()
                val userNavActions = UserNavActions(usernavController)
                UserHomePage(navActions = userNavActions, userViewModel = userViewModel, itemFetcher = itemFetcher)
            }*/

            // Step 3: Navigate to the USER_HOME route
            composeTestRule.runOnIdle {
                usernavController.navigate(NavigationRoutes.USER_HOME)
            }

            // Step 4: Assert navigation to USER_HOME
            composeTestRule.runOnIdle {
                assertEquals(NavigationRoutes.USER_HOME, usernavController.currentDestination?.route)
            }

            // Step 7: Fetch the list of items and ensure the created item is in it
            //val itemList = fakeItemFetcher.fetchItems()
            //assertTrue("Created item should be in the item list", itemList.contains(createdItemResponse))

            // Step 8: Check if the created item is displayed on the User Home Page
            composeTestRule.onNodeWithText("Test Item").assertExists() // Check that item name is displayed
            println("Test item is shown")
            composeTestRule.onNodeWithText("Price: $100").assertExists() // Check that price is displayed
            println("Price is shown")
            composeTestRule.onNodeWithText("Stock: 10").assertExists() // Check that stock is displayed
            println("Stock is shown")
        }

        /*composeTestRule.setContent {

            itemFetcher = ItemFetcher(RetrofitClient.createApiService(ItemApiService::class.java))
            //userNavActions.navigateToUserHome()
            usernavController = rememberNavController()
            // Initialize NavController within the Compose test context
            usernavController = rememberNavController()
            // Set up ItemFetcher with a mocked or real API service
            itemFetcher = ItemFetcher(RetrofitClient.createApiService(ItemApiService::class.java))

            // Load the AppNavGraph into the Compose environment
            AppNavGraph(
                navController = usernavController,
                buyerFetcher = BuyerFetcher(RetrofitClient.createApiService(BuyerApiService::class.java)), // Mock as needed
                companyFetcher = CompanyFetcher(RetrofitClient.createApiService(CompanyApiService::class.java)), // Mock as needed
                itemFetcher = itemFetcher
            )
        }

        // Navigate to the USER_HOME route
        composeTestRule.runOnIdle {
            usernavController.navigate(NavigationRoutes.USER_HOME)
        }

        // Assert navigation works correctly
        composeTestRule.runOnIdle {
            assertEquals(NavigationRoutes.USER_HOME, usernavController.currentDestination?.route)
        }

        val createdItem = itemFetcher.createItem(compId = 1, newItem = newItem)

        // Checks that the item was properly created
        assertEquals(newItem.name, createdItem.name)
        assertEquals(newItem.price, createdItem.price)
        assertEquals(newItem.description, createdItem.description)
        assertEquals(newItem.stock, createdItem.stock)
        assertTrue("The created item ID should be greater than 0.", (createdItem.id ?: 0) > 0)
        println("Created item: $createdItem")

        // Checks that we are on the user home page
        assertEquals(usernavController.currentDestination?.route, NavigationRoutes.USER_HOME)
    }*/
    }

