package com.sppProject.app.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sppProject.app.UserNavActions
import com.sppProject.app.api_integration.fetchers.ItemFetcher
import com.sppProject.app.data.data_class.Item
import com.sppProject.app.view.components.buttons.CreateItemButton
import com.sppProject.app.view.components.buttons.LogoutButton
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
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        loggedInCompany?.let { company ->
            Text(
                text = "Debug: Logged-in Company - ${company.name}, ID: ${company.id}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        } ?: Text(
            text = "Debug: No company logged in.",
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Text("Welcome to the Retailer Home Page")

        Spacer(modifier = Modifier.height(16.dp))

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
                    ItemCard(item = item)
                }
            }
        }
        // BottomNavigationRetailer(navActions)
    }
}

@Composable
fun BottomNavigationRetailer(navActions: UserNavActions) {
    BottomAppBar(Modifier.fillMaxWidth().height(40.dp),
        content = {
            Spacer(Modifier.width(100.dp))

            LogoutButton(onClick = { navActions.navigateToLogin() })

            Spacer(Modifier.width(120.dp))

            CreateItemButton(onClick = { navActions.navigateToCreateItem() })


        },
        containerColor = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun ItemCard(item: Item) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
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