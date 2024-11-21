package com.sppProject.app.view

import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sppProject.app.UserNavActions
import com.sppProject.app.api_integration.fetchers.ItemFetcher
import com.sppProject.app.api_integration.fetchers.ReceiptFetcher
import com.sppProject.app.data.data_class.Item
import com.sppProject.app.data.data_class.Receipt
import com.sppProject.app.view.components.BuyPageButton
import com.sppProject.app.view.components.LogoutButton
import com.sppProject.app.view.components.ReciptButton
import com.sppProject.app.viewModel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun Receipts(
    navActions: UserNavActions,
    userViewModel: UserViewModel,
    receiptFetcher: ReceiptFetcher
) {
    // Mutable state to hold the list of items
    var receip by remember { mutableStateOf<List<Receipt>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    // Fetch items when the composable is first displayed
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            receip = receiptFetcher.fetchReceipts()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Receipt")

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
            items(receip) { item ->
                ReceiptCard(item, onClick = {  })
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ReceiptCard(receipt: Receipt, onClick: () -> Unit) {
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
            Text(text = receipt.buyer.name, style = androidx.compose.material3.MaterialTheme.typography.bodyLarge)
            Text(text = receipt.items.name, style = androidx.compose.material3.MaterialTheme.typography.bodyLarge)

            //Text(text = "Price: $${item.price}", style = androidx.compose.material3.MaterialTheme.typography.bodyMedium)
            //Text(text = "Stock: ${item.stock}", style = androidx.compose.material3.MaterialTheme.typography.bodySmall)
        }
    }
}