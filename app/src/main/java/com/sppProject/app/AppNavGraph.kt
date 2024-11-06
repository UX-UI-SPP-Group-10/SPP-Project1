package com.sppProject.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sppProject.app.api_integration.fetchers.BuyerFetcher
import com.sppProject.app.api_integration.fetchers.CompanyFetcher
import com.sppProject.app.api_integration.fetchers.ItemFetcher
import com.sppProject.app.data.UserSessionManager
import com.sppProject.app.view.*
import com.sppProject.app.viewModel.UserViewModel


// Call functions from this class to navigate to different screens!
class UserNavActions(private val navController: NavHostController) {
    fun navigateToStartPage() {
        navController.navigate(NavigationRoutes.START_PAGE) {
            popUpTo(NavigationRoutes.START_PAGE) { inclusive = true }
        }
    }

    fun navigateToLogin() {
        navController.navigate(NavigationRoutes.LOGIN_PAGE) {
            popUpTo(NavigationRoutes.START_PAGE) { inclusive = true }
        }
    }

    fun navigateToUserHome() {
        // Only navigate if not already on the User Home page
        if (navController.currentDestination?.route != NavigationRoutes.USER_HOME) {
            navController.navigate(NavigationRoutes.USER_HOME) {
                // You can clear the back stack if necessary
                popUpTo(NavigationRoutes.START_PAGE) { inclusive = true }
            }
        }
    }

    fun navigateToRetailerHome() {
        if (navController.currentDestination?.route != NavigationRoutes.RETAILER_HOME) {
            navController.navigate(NavigationRoutes.RETAILER_HOME)
        }
    }

    fun navigateToCreatePage() {
        if (navController.currentDestination?.route != NavigationRoutes.CREATE_PAGE) {
            navController.navigate(NavigationRoutes.CREATE_PAGE)
        }
    }

    fun navigateToCreateItem() {
        if (navController.currentDestination?.route != NavigationRoutes.CREATE_ITEM) {
            navController.navigate(NavigationRoutes.CREATE_ITEM)
        }
    }

    fun navigateBack() {
        val currentDestination = navController.currentDestination?.route
        println("Navigating back from ${navController.currentDestination?.route}")
        if (currentDestination != null) {
            navController.popBackStack()
        }
    }

}



@Composable
fun AppNavGraph(navController: NavHostController, buyerFetcher: BuyerFetcher, companyFetcher: CompanyFetcher, itemFetcher: ItemFetcher) {
    // Create an instance of UserNavActions
    val userNavActions = UserNavActions(navController)

    val context = LocalContext.current
    val userViewModel = remember { UserViewModel(userNavActions, buyerFetcher, companyFetcher, UserSessionManager(context)) }

    NavHost(navController, startDestination = NavigationRoutes.LOGIN_PAGE) {
        composable(NavigationRoutes.START_PAGE) { StartPage(userNavActions, userViewModel) }
        composable(NavigationRoutes.LOGIN_PAGE) { LoginPage(userViewModel, userNavActions) }
        composable(NavigationRoutes.USER_HOME) { UserHomePage(userNavActions, userViewModel) }
        composable(NavigationRoutes.RETAILER_HOME) { RetailerHomePage(userNavActions, userViewModel, itemFetcher) }
        composable(NavigationRoutes.CREATE_PAGE) { CreatePage(userNavActions, buyerFetcher, companyFetcher) }
        composable(NavigationRoutes.CREATE_ITEM) { ItemPage(userNavActions, itemFetcher)}
    }
}



// Navigation routes
object NavigationRoutes {
    const val START_PAGE = "start"
    const val LOGIN_PAGE = "login"
    const val USER_HOME = "user_home"
    const val RETAILER_HOME = "retailer_home"
    const val CREATE_PAGE = "create"
    const val CREATE_ITEM = "create_item"
}