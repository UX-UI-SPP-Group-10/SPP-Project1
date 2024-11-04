package com.sppProject.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sppProject.app.api_integration.fetchers.BuyerFetcher
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

    fun navigateBack() {
        navController.popBackStack()
    }
}



@Composable
fun AppNavGraph(navController: NavHostController, buyerFetcher: BuyerFetcher) {
    NavHost(navController, startDestination = NavigationRoutes.START_PAGE) {
        composable(NavigationRoutes.START_PAGE) { StartPage(navController) }
        composable(NavigationRoutes.LOGIN_PAGE) { LoginPage(buyerFetcher) { navController.navigate(NavigationRoutes.USER_HOME) } }
        composable(NavigationRoutes.USER_HOME) { UserHomePage(backToLogin = { navController.navigate(NavigationRoutes.LOGIN_PAGE) }) }
        composable(NavigationRoutes.RETAILER_HOME) { RetailerHomePage(backToLogin = { navController.navigate(NavigationRoutes.LOGIN_PAGE) }) }
    }
}

// Navigation routes
object NavigationRoutes {
    const val START_PAGE = "start"
    const val LOGIN_PAGE = "login"
    const val USER_HOME = "user_home"
    const val RETAILER_HOME = "retailer_home"
}


//object UserNavActions {
//    fun navigateToHome(navController: NavHostController) {
//        navController.navigate(NavigationRoutes.HOME)
//    }
//
//    fun navigateToLogin(navController: NavHostController) {
//        navController.navigate(NavigationRoutes.LOGIN)
//    }
//}