package com.sppProject.app.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sppProject.app.NavigationRoutes
import com.sppProject.app.UserNavActions
import com.sppProject.app.model.api_integration.fetchers.BuyerFetcher
import com.sppProject.app.model.api_integration.fetchers.CompanyFetcher
import com.sppProject.app.model.api_integration.fetchers.ItemFetcher
import com.sppProject.app.model.api_integration.fetchers.ReceiptFetcher
import com.sppProject.app.model.data.UserSessionManager
import com.sppProject.app.view.components.BottomNavigationBar
import com.sppProject.app.viewModel.UserViewModel

@Composable
fun MainScreen(
    userNavActions: UserNavActions,
    userViewModel: UserViewModel,
    itemFetcher: ItemFetcher,
    buyerFetcher: BuyerFetcher,
    companyFetcher: CompanyFetcher,
    receiptFetcher: ReceiptFetcher,
    startDestination: String
) {
    val nestedNavController = rememberNavController()

    // Update the NavController in UserNavActions for nested navigation
    val nestedNavActions = UserNavActions(nestedNavController)

    Scaffold(
        bottomBar = {
            Column {
                // Adding a small line above the BottomNavigationBar
                Divider(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                )
                BottomNavigationBar(navActions = nestedNavActions, userViewModel = userViewModel)
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = nestedNavController,
                startDestination = startDestination
            ) {
                composable(NavigationRoutes.USER_HOME) {
                    UserHomePage(nestedNavActions, userViewModel, itemFetcher)
                }
                composable(NavigationRoutes.RETAILER_HOME) {
                    RetailerHomePage(nestedNavActions, userViewModel, itemFetcher)
                }
                composable(NavigationRoutes.COMPANY_RECEIPTS) {
                    CompanyReceipts(nestedNavActions, userViewModel, receiptFetcher)
                }
                composable(
                    "${NavigationRoutes.VIEW_ITEM}/{itemId}",
                    arguments = listOf(navArgument("itemId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val itemId = backStackEntry.arguments?.getLong("itemId") ?: return@composable
                    ItemViewPage(nestedNavActions, itemId, itemFetcher, receiptFetcher, userViewModel)
                }
                composable(NavigationRoutes.CREATE_ITEM) {
                    ItemPage(nestedNavActions, itemFetcher, UserSessionManager(LocalContext.current))
                }
                composable(NavigationRoutes.RECEIPTS){
                    Receipts(nestedNavActions, userViewModel, receiptFetcher, UserSessionManager(LocalContext.current))
                }
                composable(
                    "${NavigationRoutes.VIEW_RECEIPT}/{receiptId}",
                    arguments = listOf(navArgument("receiptId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val receiptId = backStackEntry.arguments?.getLong("receiptId") ?: return@composable
                    ReceiptViewPage(nestedNavActions, receiptId, receiptFetcher)
                }
                composable(
                    "${NavigationRoutes.EDIT_ITEM}/{itemId}",
                    arguments = listOf(navArgument("itemId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val itemId = backStackEntry.arguments?.getLong("itemId") ?: return@composable
                    EditItemPage(
                        itemId = itemId,
                        userNavActions = nestedNavActions,
                        itemFetcher = itemFetcher
                    )
                }


            }
        }
    }
}






