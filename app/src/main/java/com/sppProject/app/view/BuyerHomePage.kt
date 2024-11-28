package com.sppProject.app.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sppProject.app.UserNavActions
import com.sppProject.app.model.api_integration.fetchers.ItemFetcher
import com.sppProject.app.model.data.data_class.Item
import com.sppProject.app.view.components.ItemCard
import com.sppProject.app.view.components.buttons.BuyPageButton
import com.sppProject.app.view.components.buttons.LogoutButton
import com.sppProject.app.view.components.buttons.ReciptButton
import com.sppProject.app.viewModel.ItemViewModel
import com.sppProject.app.viewModel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun UserHomePage(
    itemViewModel: ItemViewModel
) {
    // Local state for items and loading
    var items by remember { mutableStateOf<List<Item>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    // Fetch items when the composable is first displayed
    LaunchedEffect(Unit) {
        loading = true
        try {
            val fetchedItems = itemViewModel.fetchItemsSync() // Synchronous fetch
            items = fetchedItems.filter { it.stock > 0 } // Filter out items with zero stock
        } catch (e: Exception) {
            // Handle error, e.g., log or show a message
            e.printStackTrace()
        } finally {
            loading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the User Home Page")

        Spacer(modifier = Modifier.height(16.dp))

        // Show loading indicator or item grid
        if (loading) {
            Text("Loading items...")
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .weight(1f),  // Gives grid weight to fill remaining space
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items) { item ->
                    ItemCard(
                        item = item,
                        onClick = { itemViewModel.userNavActions.navigateToViewItem(item) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
