package com.sppProject.app.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemViewPage(userNavActions: UserNavActions, itemId: Long, itemFetcher: ItemFetcher) {
    val itemState = remember { mutableStateOf<Item?>(null) }

    // Launch the side effect (fetch item) when the itemId changes
    LaunchedEffect(itemId) {
        val item = itemFetcher.getItemById(itemId) // Fetch item by ID
        itemState.value = item
    }

    if (itemState.value == null) {
        CircularProgressIndicator()
    } else {
        val item = itemState.value!!
        Text(text = "Item: ${item.name}")

    }
}


@Composable
private fun ItemContent(currentItem: Item) {

}