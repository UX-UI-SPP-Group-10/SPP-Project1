package com.sppProject.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sppProject.app.api_integration.fetchers.BuyerFetcher
import com.sppProject.app.view.StartPage
import com.sppProject.app.view.LoginPage

@Composable
fun AppNavGraph(navController: NavHostController, buyerFetcher: BuyerFetcher) {
    NavHost(navController, startDestination = NavigationRoutes.START_PAGE) {
        composable(NavigationRoutes.START_PAGE) { StartPage(navController) }
        composable(NavigationRoutes.LOGIN) { LoginPage(buyerFetcher = buyerFetcher) }
        // Add other composable destinations here
    }
}

object NavigationRoutes {
    const val START_PAGE = "start"
    const val LOGIN = "login"
    const val HOME = "home"
    // Add other routes here as needed
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