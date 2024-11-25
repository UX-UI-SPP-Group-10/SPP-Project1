package com.sppProject.app.view

import android.util.Log
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sppProject.app.UserNavActions
import com.sppProject.app.model.api_integration.fetchers.ReceiptFetcher
import com.sppProject.app.model.data.UserSessionManager
import com.sppProject.app.model.data.data_class.Buyer
import com.sppProject.app.model.data.data_class.Receipt
import com.sppProject.app.view.components.ReceiptCard
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
fun CompanyReceipts(
    navActions: UserNavActions,
    userViewModel: UserViewModel,
    receiptFetcher: ReceiptFetcher,
) {
    var receiptState by remember { mutableStateOf<List<Receipt>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()
    val loggedInCompany = userViewModel.companyState.collectAsState().value

    // Fetch receipts for the company when the composable is displayed
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            receiptState = receiptFetcher.fetchReceiptsByCompanyId(loggedInCompany?.id ?: 0)
        }
        for (receipt in receiptState) {
            Log.d("CompanyReceipt", "Receipt: $receipt")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Receipts for ${loggedInCompany?.name ?: "Company Name Missing"}",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(receiptState) { receipt ->
                ReceiptCard(receipt, onClick = { navActions.navigateToViewReceipt(receipt) })
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
