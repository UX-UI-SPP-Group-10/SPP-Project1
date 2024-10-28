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
    var feedbackMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Name Input Field
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Enter Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Submit Button
        Button(onClick = {
            if (name.isNotBlank()) {
                coroutineScope.launch {
                    buyerFetcher.createBuyer(Buyer(id = 0, name = name)) { newBuyer, error ->
                        feedbackMessage = if (newBuyer != null) {
                            buyers = buyers + newBuyer
                            name = "" // Clear name input after successful submission
                            "Buyer added successfully!"
                        } else {
                            error ?: "An error occurred while adding buyer."
                        }
                    }
                }
            } else {
                feedbackMessage = "Name cannot be empty."
            }
        }) {
            Text("Submit Name")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Fetch Buyers Button
        Button(onClick = {
            coroutineScope.launch {
                buyerFetcher.fetchBuyers { fetchedBuyers, error ->
                    feedbackMessage = if (error == null) {
                        buyers = fetchedBuyers
                        if (buyers.isNotEmpty()) "Buyers fetched successfully!" else "No buyers found."
                    } else {
                        error
                    }
                }
            }
        }) {
            Text("Fetch Buyers")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display feedback message
        if (feedbackMessage.isNotBlank()) {
            Text(feedbackMessage, color = if (feedbackMessage.contains("error", true)) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Display buyers list
        Text("Buyers List:")
        buyers.forEach { buyer ->
            Text("ID: ${buyer.id}, Name: ${buyer.name}")
        }
    }
}
