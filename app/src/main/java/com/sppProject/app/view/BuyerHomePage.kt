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
import com.sppProject.app.viewModel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun UserHomePage(
    navActions: UserNavActions,
    userViewModel: UserViewModel,
    itemFetcher: ItemFetcher
) {
    // Mutable state to hold the list of items
    var items by remember { mutableStateOf<List<Item>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()
    val buyerState by userViewModel.buyerState.collectAsState()

    // Fetch items when the composable is first displayed
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            items = itemFetcher.fetchItems() // Fetch items from the API
        }
    }

    val userInfo by userViewModel.buyerState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the User Home Page")


        Spacer(modifier = Modifier.height(16.dp))

        // Display grid of items
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth().padding(16.dp)
                .weight(1f),  // Gives grid weight to fill remaining space
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items.filter { it.stock > 0 }) { item ->
                ItemCard(item = item, onClick = { navActions.navigateToViewItem(item) })
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // BottomNavigation(navActions)
    }
}