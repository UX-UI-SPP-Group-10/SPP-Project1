package com.sppProject.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sppProject.app.api_integration.fetchers.BuyerFetcher
import com.sppProject.app.api_integration.fetchers.CompanyFetcher
import com.sppProject.app.api_integration.fetchers.ItemFetcher
import com.sppProject.app.view.CreatePage
import com.sppProject.app.view.StartPage
import com.sppProject.app.view.LoginPage
import com.sppProject.app.view.RetailerHomePage
import com.sppProject.app.view.UserHomePage


// Call functions from this class to navigate to different screens!
class UserNavActions(private val navController: NavHostController) {
    fun navigateToStartPage() {
        navController.navigate(NavigationRoutes.START_PAGE) {
            // Clear the back stack to prevent returning to the start page
            popUpTo(NavigationRoutes.START_PAGE) { inclusive = true }
        }
    }

    fun navigateToLogin() {
        navController.navigate(NavigationRoutes.LOGIN_PAGE)
    }

    fun navigateToUserHome() {
        navController.navigate(NavigationRoutes.USER_HOME)
    }

    fun navigateToRetailerHome() {
        navController.navigate(NavigationRoutes.RETAILER_HOME)
    }

    fun navigateToCreatePage() {
        navController.navigate(NavigationRoutes.CREATE_PAGE)
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

    NavHost(navController, startDestination = NavigationRoutes.START_PAGE) {
        composable(NavigationRoutes.START_PAGE) { StartPage(userNavActions) }
        composable(NavigationRoutes.LOGIN_PAGE) { LoginPage(buyerFetcher, userNavActions) }
        composable(NavigationRoutes.USER_HOME) { UserHomePage(userNavActions) }
        composable(NavigationRoutes.RETAILER_HOME) { RetailerHomePage(userNavActions) }
        composable(NavigationRoutes.CREATE_PAGE) { CreatePage(userNavActions, buyerFetcher, companyFetcher) }
    }
}



// Navigation routes
object NavigationRoutes {
    const val START_PAGE = "start"
    const val LOGIN_PAGE = "login"
    const val USER_HOME = "user_home"
    const val RETAILER_HOME = "retailer_home"
    const val CREATE_PAGE = "create"
}