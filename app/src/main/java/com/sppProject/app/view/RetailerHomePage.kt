package com.sppProject.app.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sppProject.app.UserNavActions
import com.sppProject.app.api_integration.fetchers.ItemFetcher
import com.sppProject.app.data.data_class.Item
import com.sppProject.app.viewModel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun RetailerHomePage(navActions: UserNavActions, userViewModel: UserViewModel, itemFetcher: ItemFetcher
) {
    // State to hold items and loading status
    var items by remember { mutableStateOf<List<Item>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    // Company ID to filter items by (hardcoded to 1 for this example)

    val loggedInCompany by userViewModel.companyState.collectAsState()

    // Fetch items when the Composable is first displayed
    LaunchedEffect(loggedInCompany) {
        loggedInCompany?.let { company ->
            loading = true
            coroutineScope.launch {
                try {
                    items = itemFetcher.fetchItemsByCompanyId(loggedInCompany!!.id?: -1L)
                } catch (e: Exception) {
                    // Handle error, e.g., show a message
                    e.printStackTrace()
                } finally {
                    loading = false
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the Retailer Home Page")

        if (loading) {
            Text("Loading items...")
        } else {
            // Display the list of items
            items.forEach { item ->
                Text("Item: ${item.name}, Price: ${item.price}, Stock: ${item.stock}")
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = {
                navActions.navigateToCreateItem()
            },
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Text("Create Listing")
        }

        Button(
            onClick = {
                userViewModel.logout()
            }, // Use NavController to go back
            modifier = Modifier.align(Alignment.BottomStart)
        ) {
            Text("Log Out")
        }
    }
}