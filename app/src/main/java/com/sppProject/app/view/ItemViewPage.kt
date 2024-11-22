package com.sppProject.app.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.sppProject.app.data.UserSessionManager
import com.sppProject.app.data.data_class.Buyer
import com.sppProject.app.data.data_class.Item
import com.sppProject.app.view.components.buttons.BackButton
import com.sppProject.app.view.components.buttons.CustomButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemViewPage(userNavActions: UserNavActions, itemId: Long, itemFetcher: ItemFetcher, receiptFetcher: ReceiptFetcher, userSessionManager: UserSessionManager) {
    val itemState = remember { mutableStateOf<Item?>(null) }
    val coroutineScope = rememberCoroutineScope()
    var isRecieptmade by remember { mutableStateOf(false) }
    var noMoreItems by remember { mutableStateOf(false) }

    // Fetch the item when the composable is first displayed or itemId changes
    LaunchedEffect(itemId) {
        val item = itemFetcher.getItemById(itemId) // Fetch item by ID
        itemState.value = item
    }

    Scaffold(
        topBar = { ItemTopAppBar(userNavActions) },
        content = { paddingValues ->
            ItemContent(itemState.value, paddingValues)
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                if(noMoreItems){
                    Text(modifier = Modifier.align(Alignment.TopCenter),
                        text = "There is no more of this item available")
                }
                CustomButton(
                    onClick = {
                        isRecieptmade = true
                    },
                    text = "Reserve Item",
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .width(200.dp)
                )

            }
        }
    )


    if (isRecieptmade){
        if ((itemState.value?.stock ?: 0) > 0){
            PostReceipt(receiptFetcher, itemId, itemFetcher, userSessionManager, coroutineScope)
            userNavActions.navigateUserHome()
            isRecieptmade = false
        }
        else {
            noMoreItems = true
            isRecieptmade = false
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemTopAppBar(userNavActions: UserNavActions) {
    TopAppBar(
        navigationIcon = {
            BackButton(onClick = { userNavActions.navigateBack() })
        },
        title = { Text("Item Details") },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,   // Background color
            titleContentColor = MaterialTheme.colorScheme.onPrimary // Content (title) color
        )
    )
}

@Composable
fun ItemContent(item: Item?, paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        // Show loading indicator until the item is fetched
        if (item == null) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            ItemDetails(item)
        }
    }
}

@Composable
fun ItemDetails(item: Item) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Item: ${item.name}",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Price: $${item.price}",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Stock: ${item.stock}",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Description: ${item.description}",
            style = MaterialTheme.typography.bodyMedium
        )

    }
}

@Composable
fun PostReceipt(
    receiptFetcher: ReceiptFetcher,
    itemID: Long,
    itemFetcher: ItemFetcher,
    userSessionManager: UserSessionManager,
    coroutineScope: CoroutineScope
) {
    val currentUser: Buyer? = userSessionManager.getLoggedInBuyer()
    LaunchedEffect(itemID) {
        // Launch the coroutine only when `tempItem` changes (or you can check some other condition)
        coroutineScope.launch {
            receiptFetcher.createReceipt(currentUser?.id ?: 0,itemID)
        }
    }
}