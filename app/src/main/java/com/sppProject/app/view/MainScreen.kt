package com.sppProject.app.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sppProject.app.NavigationRoutes
import com.sppProject.app.UserNavActions
import com.sppProject.app.api_integration.fetchers.BuyerFetcher
import com.sppProject.app.api_integration.fetchers.CompanyFetcher
import com.sppProject.app.api_integration.fetchers.ItemFetcher
import com.sppProject.app.data.UserSessionManager
import com.sppProject.app.view.components.BottomNavigationBar
import com.sppProject.app.viewModel.UserViewModel

@Composable
fun MainScreen(
    userNavActions: UserNavActions,
    userViewModel: UserViewModel,
    itemFetcher: ItemFetcher,
    buyerFetcher: BuyerFetcher,
    companyFetcher: CompanyFetcher,
    startDestination: String
) {
    val nestedNavController = rememberNavController() // Create a new NavController for nested navigation

    Scaffold(
        bottomBar = {
            BottomNavigationBar(userNavActions) // Persistent bottom navigation bar
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = nestedNavController, // Use nestedNavController for nested navigation
                startDestination = startDestination // Use the parameter startDestination
            ) {
                composable(NavigationRoutes.USER_HOME) {
                    UserHomePage(userNavActions, userViewModel, itemFetcher)
                }
                composable(NavigationRoutes.RETAILER_HOME) {
                    RetailerHomePage(userNavActions, userViewModel, itemFetcher)
                }
                composable(NavigationRoutes.CREATE_PAGE) {
                    CreateProfilePage(userNavActions, buyerFetcher = buyerFetcher, companyFetcher = companyFetcher)
                }
                composable(
                    "${NavigationRoutes.VIEW_ITEM}/{itemId}",
                    arguments = listOf(navArgument("itemId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val itemId = backStackEntry.arguments?.getLong("itemId") ?: return@composable
                    ItemViewPage(userNavActions, itemId, itemFetcher)
                }
            }
        }
    }
}




