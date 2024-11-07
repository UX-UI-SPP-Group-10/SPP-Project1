package com.sppProject.app.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
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
import com.sppProject.app.api_integration.fetchers.ItemFetcher
import com.sppProject.app.data.data_class.Item
import com.sppProject.app.view.components.BackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemViewPage(userNavActions: UserNavActions, itemId: Long, itemFetcher: ItemFetcher) {
    val itemState = remember { mutableStateOf<Item?>(null) }

    // Fetch the item when the composable is first displayed or itemId changes
    LaunchedEffect(itemId) {
        val item = itemFetcher.getItemById(itemId) // Fetch item by ID
        itemState.value = item
    }

    Scaffold(
        topBar = { ItemTopAppBar(userNavActions) },
        content = { paddingValues ->
            ItemContent(itemState.value, paddingValues)
        }
    )
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
    }
}