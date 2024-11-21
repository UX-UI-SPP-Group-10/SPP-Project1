package com.sppProject.app.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sppProject.app.UserNavActions
import com.sppProject.app.api_integration.fetchers.ItemFetcher
import com.sppProject.app.data.data_class.Item
import com.sppProject.app.view.components.CustomButton
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
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Debug line for user information
        buyerState?.let { buyer ->
            Text(
                text = "Debug: Logged-in User - ${buyer.name}, ID: ${buyer.id}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        } ?: Text(
            text = "Debug: No user logged in.",
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Text("Welcome to the User Home Page")


        Spacer(modifier = Modifier.height(16.dp))

        // Display grid of items
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),  // Gives grid weight to fill remaining space
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items) { item ->
                ItemCard(item = item, onClick = { navActions.navigateToViewItem(item) })
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        CustomButton(
            onClick = { userViewModel.logout() },
            text = "Log Out",
            modifier = Modifier.align(Alignment.Start)
        )
    }
}

@Composable
private fun ItemCard(item: Item, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = item.name, style = androidx.compose.material3.MaterialTheme.typography.bodyLarge)
            Text(text = "Price: $${item.price}", style = androidx.compose.material3.MaterialTheme.typography.bodyMedium)
            Text(text = "Stock: ${item.stock}", style = androidx.compose.material3.MaterialTheme.typography.bodySmall)
        }
    }
}
