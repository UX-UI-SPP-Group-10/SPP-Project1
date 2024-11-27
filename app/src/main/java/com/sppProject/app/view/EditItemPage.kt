package com.sppProject.app.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sppProject.app.UserNavActions
import com.sppProject.app.model.api_integration.fetchers.ItemFetcher
import com.sppProject.app.model.data.data_class.Item
import com.sppProject.app.view.components.CustomTextField
import com.sppProject.app.view.components.buttons.BackButton
import com.sppProject.app.view.components.buttons.CustomButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditItemPage(
    itemId: Long,
    userNavActions: UserNavActions,
    itemFetcher: ItemFetcher
) {
    val itemState = remember { mutableStateOf<Item?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val itemName = remember { mutableStateOf("") }
    val itemPrice = remember { mutableStateOf(0) }
    val itemStock = remember { mutableStateOf(0) }
    val itemDescription = remember { mutableStateOf("") }

    // Fetch the item details when the composable is displayed
    LaunchedEffect(itemId) {
        coroutineScope.launch {
            val item = itemFetcher.getItemById(itemId)
            itemState.value = item
            itemName.value = item?.name ?: ""
            itemPrice.value = item?.price ?: 0
            itemStock.value = item?.stock ?: 0
            itemDescription.value = item?.description ?: ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
            navigationIcon = {
                BackButton(onClick = { userNavActions.navigateBack() })
            },
            title = { Text("Edit Item") }
        )},
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                CustomTextField(
                    value = itemName.value,
                    onValueChange = { itemName.value = it },
                    labelText = "Item Name",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                CustomTextField(
                    value = itemPrice.value.toString(),
                    onValueChange = { itemPrice.value = it.toIntOrNull() ?: itemPrice.value },
                    labelText = "Price",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                CustomTextField(
                    value = itemStock.value.toString(),
                    onValueChange = { itemStock.value = it.toIntOrNull() ?: itemStock.value },
                    labelText = "Stock",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                CustomTextField(
                    value = itemDescription.value,
                    onValueChange = { itemDescription.value = it },
                    labelText = "Description",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                CustomButton(
                    onClick = {
                        coroutineScope.launch {
                            itemFetcher.updateItem(
                                itemId,
                                Item(
                                    name = itemName.value,
                                    price = itemPrice.value,
                                    stock = itemStock.value,
                                    description = itemDescription.value
                                )
                            )
                            userNavActions.navigateBack()
                        }
                    },
                    text = "Save Changes",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}

