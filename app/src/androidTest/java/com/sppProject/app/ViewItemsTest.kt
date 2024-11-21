package com.sppProject.app

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
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

    private lateinit var itemFetcher: ItemFetcher
    //private lateinit var userNavActions : UserNavActions
    private lateinit var usernavController: NavHostController

    @Test
    fun userViewTest() = runBlocking() {

        composeTestRule.setContent {

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

        val newItem = Item(
            name = "Test Item",
            price = 100,
            description = "A test item description",
            stock = 10,
        )

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
    }
}
