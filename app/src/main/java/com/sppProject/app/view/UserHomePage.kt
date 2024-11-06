package com.sppProject.app.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sppProject.app.UserNavActions
import com.sppProject.app.data.data_class.Item
import com.sppProject.app.view.components.CustomButton
import com.sppProject.app.viewModel.UserViewModel

@Composable
fun UserHomePage(navActions: UserNavActions, userViewModel: UserViewModel) {
    // Sample items to display
    val items = listOf(
        Item(name = "Item 1", price = 100, description = "Description 1", stock = 5),
        Item(name = "Item 2", price = 200, description = "Description 2", stock = 3),
        Item(name = "Item 3", price = 150, description = "Description 3", stock = 10),
        Item(name = "Item 4", price = 120, description = "Description 4", stock = 8),
        Item(name = "Item 5", price = 180, description = "Description 5", stock = 2),
        Item(name = "Item 6", price = 220, description = "Description 6", stock = 6),
        Item(name = "Item 7", price = 130, description = "Description 7", stock = 7),
        Item(name = "Item 8", price = 160, description = "Description 8", stock = 4),
        Item(name = "Item 9", price = 250, description = "Description 9", stock = 12),
        Item(name = "Item 10", price = 110, description = "Description 10", stock = 9),
        Item(name = "Item 11", price = 195, description = "Description 11", stock = 11),
        Item(name = "Item 12", price = 300, description = "Description 12", stock = 15),
        Item(name = "Item 13", price = 140, description = "Description 13", stock = 6),
        Item(name = "Item 14", price = 275, description = "Description 14", stock = 3),
        Item(name = "Item 15", price = 310, description = "Description 15", stock = 1),
        Item(name = "Item 16", price = 90, description = "Description 16", stock = 20),
        Item(name = "Item 17", price = 125, description = "Description 17", stock = 8),
        Item(name = "Item 18", price = 210, description = "Description 18", stock = 7),
        Item(name = "Item 19", price = 240, description = "Description 19", stock = 5),
        Item(name = "Item 20", price = 190, description = "Description 20", stock = 4),
        Item(name = "Item 21", price = 175, description = "Description 21", stock = 9),
        Item(name = "Item 22", price = 230, description = "Description 22", stock = 2),
        Item(name = "Item 23", price = 270, description = "Description 23", stock = 10),
        Item(name = "Item 24", price = 330, description = "Description 24", stock = 3),
        Item(name = "Item 25", price = 185, description = "Description 25", stock = 1),
        Item(name = "Item 26", price = 195, description = "Description 26", stock = 6),
        Item(name = "Item 27", price = 220, description = "Description 27", stock = 13),
        Item(name = "Item 28", price = 165, description = "Description 28", stock = 8),
        Item(name = "Item 29", price = 240, description = "Description 29", stock = 14),
        Item(name = "Item 30", price = 130, description = "Description 30", stock = 11),
        Item(name = "Item 31", price = 180, description = "Description 31", stock = 2),
        Item(name = "Item 32", price = 260, description = "Description 32", stock = 9),
        Item(name = "Item 33", price = 210, description = "Description 33", stock = 5),
        Item(name = "Item 34", price = 145, description = "Description 34", stock = 7),
        Item(name = "Item 35", price = 290, description = "Description 35", stock = 1)
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the User Home Page")

        Spacer(modifier = Modifier.height(16.dp))

        // Display grid of items
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),  // Gives grid weight to fill remaining space
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items) { item ->
                ItemCard(item = item)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        CustomButton(
            onClick = { userViewModel.logout() },
            text = "Log Out",
            modifier = Modifier.align(Alignment.Start)
        )
    }
}

@Composable
fun ItemCard(item: Item) {
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
