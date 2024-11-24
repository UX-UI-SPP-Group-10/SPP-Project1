package com.sppProject.app.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sppProject.app.UserNavActions
import com.sppProject.app.api_integration.fetchers.ReceiptFetcher
import com.sppProject.app.data.data_class.Receipt
import com.sppProject.app.view.components.buttons.BackButton

@Composable
fun ReceiptViewPage(userNavActions: UserNavActions, receiptID: Long, receiptFetcher: ReceiptFetcher) {
    val receiptState = remember { mutableStateOf<Receipt?>(null) }

    // Fetch the item when the composable is first displayed or itemId changes
    LaunchedEffect(receiptID) {
        val receipt = receiptFetcher.fetchReceiptById(receiptID) // Fetch item by ID
        receiptState.value = receipt
    }

    Scaffold(
        topBar = { ReceiptTopAppBar(userNavActions) },
        content = { paddingValues ->
            ReceiptContent(receiptState.value, paddingValues)
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptTopAppBar(userNavActions: UserNavActions) {
    TopAppBar(
        navigationIcon = {
            BackButton(onClick = { userNavActions.navigateBack() })
        },
        title = { Text("Receipt Details") },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,   // Background color
            titleContentColor = MaterialTheme.colorScheme.secondary // Content (title) color
        )
    )
}

@Composable
fun ReceiptContent(receipt: Receipt?, paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        // Show loading indicator until the item is fetched
        if (receipt == null) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            ReceiptDetails(receipt)
        }
    }
}

@Composable
fun ReceiptDetails(receipt: Receipt) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Company: ${receipt.item.company}",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Item: ${receipt.item.name}",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Price: $${receipt.item.price}",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Description: ${receipt.item.description}",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "id: ${receipt.id}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}