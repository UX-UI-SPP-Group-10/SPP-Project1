package com.sppProject.app.view

import android.util.Log
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
import com.sppProject.app.model.api_integration.fetchers.ItemFetcher
import com.sppProject.app.model.data.data_class.Item
import com.sppProject.app.view.components.ItemCard
import com.sppProject.app.view.components.buttons.CreateItemButton
import com.sppProject.app.view.components.buttons.CustomButton
import com.sppProject.app.view.components.buttons.LogoutButton
import com.sppProject.app.viewModel.ItemViewModel
import com.sppProject.app.viewModel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun RetailerHomePage(itemViewModel: ItemViewModel) {
    // Local state for items and loading
    var items by remember { mutableStateOf<List<Item>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    val loggedInCompany by itemViewModel.userViewModel.companyState.collectAsState()

    LaunchedEffect(loggedInCompany) {
        loggedInCompany?.id?.let { companyId ->
            loading = true
            try {
                val fetchedItems = itemViewModel.fetchItemsByCompanyIDSync(companyId) // Synchronous fetch
                items = fetchedItems
            } catch (e: Exception) {
                Log.e("RetailerHomePage", "Error fetching items", e)
            } finally {
                loading = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
                    .weight(1f),
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

        CustomButton(
            onClick = { itemViewModel.userNavActions.navigateToCreateItem() },
            text = "Create Item",
            modifier = Modifier.padding(16.dp)
        )
    }
}



