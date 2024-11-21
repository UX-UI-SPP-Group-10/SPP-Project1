package com.sppProject.app.view

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.sppProject.app.api_integration.fetchers.ReceiptFetcher
import com.sppProject.app.data.UserSessionManager
import com.sppProject.app.data.data_class.Buyer
import com.sppProject.app.data.data_class.Receipt
import com.sppProject.app.viewModel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun Receipts(
    navActions: UserNavActions,
    userViewModel: UserViewModel,
    receiptFetcher: ReceiptFetcher,
    userSessionManager: UserSessionManager
) {
    // Mutable state to hold the list of items
    var receiptState by remember { mutableStateOf<List<Receipt>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()
    val currentUser: Buyer? = userSessionManager.getLoggedInBuyer()

    // Fetch items when the composable is first displayed
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            receiptState = receiptFetcher.fetchReceiptByBuyerId(currentUser?.id ?: 0)
        }
        for (receipt in receiptState) {
            Log.e("Receipt", "Receipt: $receipt")
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
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(receiptState) { item ->
                ReceiptCard(item, onClick = { navActions.navigateToViewReceipt(item) })
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

            Text(
                text = receipt.buyer.name,
                style = MaterialTheme.typography.bodyLarge
            )

            val itemName = receipt.item.name
            Text(
                text = itemName,
                style = MaterialTheme.typography.bodyLarge
            )


            //Text(text = receipt.buyer.name, style = androidx.compose.material3.MaterialTheme.typography.bodyLarge)
            //Text(text = receipt.items.name, style = androidx.compose.material3.MaterialTheme.typography.bodyLarge)

            //Text(text = "Price: $${item.price}", style = androidx.compose.material3.MaterialTheme.typography.bodyMedium)
            //Text(text = "Stock: ${item.stock}", style = androidx.compose.material3.MaterialTheme.typography.bodySmall)
        }
    }
}