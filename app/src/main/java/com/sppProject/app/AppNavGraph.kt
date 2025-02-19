package com.sppProject.app

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sppProject.app.model.api_integration.fetchers.BuyerFetcher
import com.sppProject.app.model.api_integration.fetchers.CompanyFetcher
import com.sppProject.app.model.api_integration.fetchers.ItemFetcher
import com.sppProject.app.model.api_integration.fetchers.ReceiptFetcher
import com.sppProject.app.model.data.UserSessionManager
import com.sppProject.app.model.data.data_class.Item
import com.sppProject.app.model.data.data_class.Receipt
import com.sppProject.app.view.*
import com.sppProject.app.viewModel.CreatePageViewModel
import com.sppProject.app.viewModel.UserViewModel


// Call functions from this class to navigate to different screens!
class UserNavActions(private val navController: NavHostController) {

    fun navigateToLogin() {
        navController.navigate(NavigationRoutes.LOGIN_PAGE) {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
    }

    fun navigateFromLoginToUserHome() {
        navController.navigate("${NavigationRoutes.MAIN_SCREEN}?startDestination=${NavigationRoutes.USER_HOME}") {
            popUpTo(0) { inclusive = true } // Clears entire back stack
            launchSingleTop = true
        }
    }

    fun navigateFromLoginToRetailerHome() {
        navController.navigate("${NavigationRoutes.MAIN_SCREEN}?startDestination=${NavigationRoutes.RETAILER_HOME}") {
            popUpTo(0) { inclusive = true } // Clears entire back stack
            launchSingleTop = true
        }
    }

    fun navigateToRetailerHome() {
        navController.navigate(NavigationRoutes.RETAILER_HOME) {
            popUpTo(0) { inclusive = true } // Clears entire back stack
            launchSingleTop = true
        }
    }

    fun navigateUserHome() {
        navController.navigate(NavigationRoutes.USER_HOME) {
            popUpTo(0) { inclusive = true } // Clears entire back stack
            launchSingleTop = true
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
        val itemId = item.id
        navController.navigate("${NavigationRoutes.VIEW_ITEM}/$itemId")
    }

    fun navigateToUserReceipts() {
        navController.navigate(NavigationRoutes.RECEIPTS)
    }

    fun navigateToViewReceipt(receipt: Receipt) {
        val receiptId = receipt.id
        navController.navigate("${NavigationRoutes.VIEW_RECEIPT}/$receiptId")
    }

    fun navigateToEditItem(itemId: Long) {
        navController.navigate("${NavigationRoutes.EDIT_ITEM}/$itemId")
    }

    fun navigateToCompanyReceipts() {
        navController.navigate(NavigationRoutes.COMPANY_RECEIPTS)
    }

    fun navigateBack() {
        Log.d("NavigationDebug", "Attempting to navigate back")
        Log.d("NavigationDebug", "Current back stack: ${navController.backQueue.map { it.destination.route }}")

        if (!navController.popBackStack()) {
            Log.d("NavigationDebug", "popBackStack failed, falling back to navigateUp")
            navController.navigateUp()
        } else {
            Log.d("NavigationDebug", "popBackStack successful")
        }
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
    val createPageViewModel = remember { CreatePageViewModel(userSessionManager, buyerFetcher, companyFetcher) }

    NavHost(navController = navController, startDestination = NavigationRoutes.LOGIN_PAGE) {
        composable(NavigationRoutes.LOGIN_PAGE) {
            LoginPage(userViewModel, userNavActions)
        }

        composable(NavigationRoutes.CREATE_PROFILE_PAGE) {
            CreateProfilePage(userNavActions, buyerFetcher, companyFetcher, createPageViewModel)
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
                receiptFetcher = receiptFetcher,
                startDestination = startDestination,
            )
        }
    }
}



// Navigation routes
object NavigationRoutes {
    const val LOGIN_PAGE = "login"
    const val CREATE_PROFILE_PAGE = "create_profile"
    const val MAIN_SCREEN = "main_screen"
    const val USER_HOME = "user_home"
    const val RETAILER_HOME = "retailer_home"
    const val CREATE_ITEM = "create_item"
    const val VIEW_ITEM = "view_item"
    const val RECEIPTS = "receipts"
    const val VIEW_RECEIPT = "view_receipt"
    const val EDIT_ITEM = "edit_item"
    const val COMPANY_RECEIPTS = "company_receipts"
}

