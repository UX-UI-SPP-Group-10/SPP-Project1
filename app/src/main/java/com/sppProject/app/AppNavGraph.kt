package com.sppProject.app

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sppProject.app.api_integration.fetchers.BuyerFetcher
import com.sppProject.app.api_integration.fetchers.CompanyFetcher
import com.sppProject.app.api_integration.fetchers.ItemFetcher
import com.sppProject.app.api_integration.fetchers.ReceiptFetcher
import com.sppProject.app.data.UserSessionManager
import com.sppProject.app.data.data_class.Item
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

    fun navigateFromLoginToUserHome() {
        navController.navigate("${NavigationRoutes.MAIN_SCREEN}?startDestination=${NavigationRoutes.USER_HOME}") {
            popUpTo(NavigationRoutes.LOGIN_PAGE) { inclusive = true }
        }
    }

    fun navigateFromLoginToRetailerHome() {
        navController.navigate("${NavigationRoutes.MAIN_SCREEN}?startDestination=${NavigationRoutes.RETAILER_HOME}") {
            popUpTo(NavigationRoutes.LOGIN_PAGE) { inclusive = true }
        }
    }

    fun navigateToRetailerHome() {
        navController.navigate(NavigationRoutes.RETAILER_HOME) {
            popUpTo(NavigationRoutes.LOGIN_PAGE) { inclusive = true }
        }
    }

    fun navigateUserHome() {
        navController.navigate(NavigationRoutes.USER_HOME) {
            popUpTo(NavigationRoutes.LOGIN_PAGE) { inclusive = true }
        }
    }


    fun navigateToCreateProfile() {
        navController.navigate(NavigationRoutes.CREATE_PROFILE_PAGE)
    }


    fun navigateToCreateItem() {
        Log.d("UserNavActions", "Navigating to create a new item page")
        navController.navigate(NavigationRoutes.CREATE_ITEM)
    }


    fun navigateToViewItem(item: Item) {
        if (navController.currentDestination?.route != NavigationRoutes.VIEW_ITEM) {
            val itemId = item.id
            navController.navigate("${NavigationRoutes.VIEW_ITEM}/$itemId")
        }
    }

    fun navigateToUserReceipts() {
        navController.navigate(NavigationRoutes.RECEIPTS)
    }


    fun navigateBack() {
        navController.popBackStack()
    }

}


@Composable
fun AppNavGraph(
    navController: NavHostController,
    buyerFetcher: BuyerFetcher,
    companyFetcher: CompanyFetcher,
    itemFetcher: ItemFetcher,
    receiptFetcher: ReceiptFetcher
) {
    val context = LocalContext.current
    val userSessionManager = UserSessionManager(context)
    val userNavActions = UserNavActions(navController)
    val userViewModel = remember { UserViewModel(userNavActions, buyerFetcher, companyFetcher, userSessionManager) }

    NavHost(navController = navController, startDestination = NavigationRoutes.LOGIN_PAGE) {
        composable(NavigationRoutes.LOGIN_PAGE) {
            LoginPage(userViewModel, userNavActions)
        }

        composable(NavigationRoutes.CREATE_PROFILE_PAGE) {
            CreateProfilePage(userNavActions, buyerFetcher, companyFetcher)
        }

        composable("${NavigationRoutes.MAIN_SCREEN}?startDestination={startDestination}",
            arguments = listOf(navArgument("startDestination") { defaultValue = NavigationRoutes.USER_HOME })
        ) { backStackEntry ->
            val startDestination = backStackEntry.arguments?.getString("startDestination")
                ?: NavigationRoutes.USER_HOME

            MainScreen(
                userNavActions = userNavActions,
                userViewModel = userViewModel,
                itemFetcher = itemFetcher,
                buyerFetcher = buyerFetcher,
                companyFetcher = companyFetcher,
                receiptFetcher = receiptFetcher,
                startDestination = startDestination,
            )
        }
    }
}







// Navigation routes
object NavigationRoutes {
    const val START_PAGE = "start"
    const val LOGIN_PAGE = "login"
    const val CREATE_PROFILE_PAGE = "create_profile"
    const val MAIN_SCREEN = "main_screen"
    const val USER_HOME = "user_home"
    const val RETAILER_HOME = "retailer_home"
    const val CREATE_ITEM = "create_item"
    const val VIEW_ITEM = "view_item"
    const val RECEIPTS = "receipts"
}

