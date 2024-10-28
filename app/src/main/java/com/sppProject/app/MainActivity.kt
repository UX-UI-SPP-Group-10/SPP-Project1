package com.sppProject.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.api_integration.RetrofitClient
import com.api_integration.ApiFetcher
import com.api_integration.data_class.Buyer
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var buyerFetcher: ApiFetcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the ApiFetcher with the BuyerApiService
        buyerFetcher = ApiFetcher(RetrofitClient.buyerApiService)

        setContent {
            SimpleBuyerApp(buyerFetcher)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleBuyerApp(buyerFetcher: ApiFetcher) {
    var buyers by remember { mutableStateOf(listOf<Buyer>()) }
    var name by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) } // For handling errors
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Simple Name Input
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Enter Name") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button to post a new name
        Button(onClick = {
            if (name.isNotBlank()) {
                coroutineScope.launch {
                    buyerFetcher.createBuyer(Buyer(id = 0, name = name)) { newBuyer, error ->
                        if (newBuyer != null) {
                            buyers = buyers + newBuyer
                            name = "" // Clear name after submission
                            message = "Buyer added successfully!"
                        } else if (error != null) {
                            errorMessage = error
                        }
                    }
                }
            } else {
                message = "Name cannot be empty."
            }
        }) {
            Text("Submit Name")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to fetch buyers
        Button(onClick = {
            coroutineScope.launch {
                buyerFetcher.fetchBuyers { fetchedBuyers, error ->
                    if (error == null) {
                        buyers = fetchedBuyers
                        message = if (buyers.isNotEmpty()) {
                            "Buyers fetched successfully!"
                        } else {
                            "No buyers found."
                        }
                    } else {
                        errorMessage = error
                    }
                }
            }
        }) {
            Text("Fetch Buyers")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display the feedback message or error message
        if (message.isNotBlank()) {
            Text(message)
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (errorMessage != null) {
            Text("Error: $errorMessage", color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Display the list of buyers
        Text("Buyers List:")
        for (buyer in buyers) {
            Text("ID: ${buyer.id}, Name: ${buyer.name}")
        }
    }
}


// Preview for BuyerApp
@Preview(showBackground = true)
@Composable
fun BuyerAppPreview() {
    // Use a mock implementation for the preview
    SimpleBuyerApp(buyerFetcher = ApiFetcher(RetrofitClient.buyerApiService))
}
